using linuxWS.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LinuxWS.Models
{
    public class FindPath
    {
        public static int ENTRY_POINT_TYPE_MASK = (1 << 0);
        public static int STAIRS_POINT_TYPE_MASK = (1 << 2);
        public static int ELEVATOR_POINT_TYPE_MASK = (1 << 3);
        public static int EMERGENCY_EXIT_POINT_TYPE_MASK = (1 << 4);
        public static int NO_QR_CODE_POINT_TYPE_MASK = (1 << 5);

        public static NextPoint GetNextPoint(int? idPrevPoint, 
            int idActualPoint, 
            int idDestPoint, 
            List<MdlPoints> points, 
            List<MdlPointsConnection> connections, 
            double scale, 
            int destinationPointLevel,
            int actualPointLevel)
        {
            var adjList = new AdjacencyList(points, connections);
            var nextPoint = new NextPoint();
            Dijkstra(idPrevPoint, adjList.StartPoints.IndexOf(idActualPoint), adjList.StartPoints.IndexOf(idDestPoint), adjList, nextPoint, points);

            nextPoint.Distance /= scale;
            nextPoint.DistanceOnAnotherLevel /= scale;

            nextPoint.Level = destinationPointLevel;
            nextPoint.CurrentLevel = actualPointLevel;

            return nextPoint;
        }

        public enum Icon
        {
            Straight = 0,
            StraightLeft = 1,
            Left = 2,
            TurnAround = 3,
            Right = 4,
            StraightRight = 5
        }

        private static bool IsElevator(MdlPoints point)
        {
            if ((point.IdPointType & ELEVATOR_POINT_TYPE_MASK) != 0)
                return true;
            return false;
        }
        private static bool IsStairs(MdlPoints point)
        {
            if ((point.IdPointType & STAIRS_POINT_TYPE_MASK) != 0)
                return true;
            return false;
        }

        private static Icon GetIcon(double angle)
        {
            if (-30 <= angle && angle <= 30)
                return Icon.Straight;

            if (-60 <= angle && angle <= -30)
                return Icon.StraightRight;
            if (-120 <= angle && angle <= -60)
                return Icon.Right;

            if (30 <= angle && angle <= 60)
                return Icon.StraightLeft;
            if (60 <= angle && angle <= 120)
                return Icon.Left;

            return Icon.TurnAround;
        }

        private static MdlPoints CalculateVirtualPoint(MdlPoints point)
        {
            double direction = Math.PI * (!(point.Direction is null) ? (double)point.Direction + 180 : 0) / 180.0;

            var returnPoint = new MdlPoints();
            const double DISTANCE = 10.0;
            var diffX = DISTANCE * Math.Sin(direction);
            var diffY = DISTANCE * Math.Cos(direction);

            returnPoint.X = point.X - diffX;
            returnPoint.Y = point.Y - diffY;

            return returnPoint;
        }

        private static void Dijkstra(int? prevPointId, int startPointId, int endPointId, AdjacencyList list, NextPoint nextPoint, List<MdlPoints> points)
        {
            var size = list.StartPoints.Count;

            var distance = new double[size];
            var set = new bool[size];
            var prev = new int[size];
            for (var i = 0; i < distance.Length; i++)
            {
                distance[i] = double.MaxValue;
                set[i] = false;
                prev[i] = -1;
            }
            distance[startPointId] = 0;
            for (var i = 0; i < size - 1; i++)
            {
                var num = MinimumDistance(distance, set, size);
                set[num] = true;
                foreach (var n in list.EndPoints[num])
                {
                    var index = list.StartPoints.IndexOf(n.Item1);
                    if (distance[index] > distance[num] + n.Item2)
                    {
                        distance[index] = distance[num] + n.Item2;
                        prev[index] = num;
                    }
                }
            }

            nextPoint.IdPoint = list.StartPoints[(int)DijkstraNextPoint(startPointId, endPointId, distance, prev)];
            if (IsStairs(points.FirstOrDefault(i => i.IdPoint == nextPoint.IdPoint)))
                nextPoint.Stairs = true;
            if (IsElevator(points.FirstOrDefault(i => i.IdPoint == nextPoint.IdPoint)))
                nextPoint.Elevator = true;

            var actualPoint = points.FirstOrDefault(i => i.IdPoint == list.StartPoints[startPointId]);
            var destPoint = points.FirstOrDefault(i => i.IdPoint == list.StartPoints[endPointId]);
            var next = points.FirstOrDefault(i => i.IdPoint == nextPoint.IdPoint);

            nextPoint.Distance = CalculateDistanceBetweenPoints(
                                     actualPoint,
                                     next);

            nextPoint.Angle = CalculateAngle(prevPointId, actualPoint, next, points);
            nextPoint.Icon = (int)GetIcon(nextPoint.Angle);

            if (nextPoint.Stairs || nextPoint.Elevator)
            {
                nextPoint.IdPoint = list.StartPoints[(int)DijkstraNextPoint(list.StartPoints.IndexOf(nextPoint.IdPoint), endPointId, distance, prev)];
                actualPoint = points.FirstOrDefault(i => i.IdPoint == nextPoint.IdPoint);
                nextPoint.IdPoint = list.StartPoints[(int)DijkstraNextPoint(list.StartPoints.IndexOf(nextPoint.IdPoint), endPointId, distance, prev)];
                destPoint = points.FirstOrDefault(i => i.IdPoint == list.StartPoints[endPointId]);
                next = points.FirstOrDefault(i => i.IdPoint == nextPoint.IdPoint);

                nextPoint.DistanceOnAnotherLevel = CalculateDistanceBetweenPoints(
                    actualPoint,
                    next);

                nextPoint.AngleOnAnotherLevel = CalculateAngle(prevPointId, actualPoint, next, points);
                nextPoint.IconOnAnotherLevel = (int)GetIcon(nextPoint.AngleOnAnotherLevel);
            }
        }

        private static int? DijkstraNextPoint(int startPointId, int endPointId, double[] distance, int[] prev)
        {
            var point = endPointId;
            while (prev[point] != startPointId)
            {
                if (distance[point] == double.MaxValue)
                    return null;
                point = prev[point];
            }
            return point;
        }

        private static int MinimumDistance(double[] distance, bool[] set, int size)
        {
            var min = double.MaxValue;
            var min_index = -1;

            for (int v = 0; v < size; v++)
            {
                if (set[v] == false && distance[v] <= min)
                {
                    min = distance[v];
                    min_index = v;
                }
            }

            return min_index;
        }

        private static double CalculateAngle(int? prevPointId, MdlPoints actualPoint, MdlPoints destPoint, List<MdlPoints> points)
        {
            if (prevPointId is null || (bool)actualPoint.OnOffDirection)
            {
                var prevPoint = CalculateVirtualPoint(actualPoint);
                return Angle((double)actualPoint.X, (double)actualPoint.Y, (double)prevPoint.X, (double)prevPoint.Y, (double)destPoint.X, (double)destPoint.Y);
            }
            else
            {
                var prevPoint = points.FirstOrDefault(i => i.IdPoint == prevPointId);
                return Angle((double)actualPoint.X, (double)actualPoint.Y, (double)prevPoint.X, (double)prevPoint.Y, (double)destPoint.X, (double)destPoint.Y);
            }
        }

        private static double Angle(double x1, double y1, double x2, double y2, double x3, double y3)
        {
            double v1Angle = Math.Atan(-(y1 - y2) / (x1 - x2)) * 180 / Math.PI;
            if ((y2 - y1) > 0)
                v1Angle = 360 + v1Angle;
            double v2Angle = Math.Atan(-(y3 - y1) / (x3 - x1)) * 180 / Math.PI;
            if ((y3 - y2) > 0)
                v2Angle = 360 + v2Angle;

            var angle = v2Angle - v1Angle;
            if (angle <= -180)
                angle += 360;
            if (angle > 180)
                angle -= 360;
            return angle;
        }

        public class AdjacencyList
        {
            public List<Tuple<int, double>>[] EndPoints { get; set; }
            public List<int> StartPoints { get; set; }

            public AdjacencyList(List<MdlPoints> points, List<MdlPointsConnection> connections)
            {
                StartPoints = new List<int>();
                EndPoints = new List<Tuple<int, double>>[points.Count];
                foreach (var conn in connections)
                {
                    if (conn.IdPointStart is null)
                        continue;
                    if (conn.IdPointEnd is null)
                        continue;
                    if (conn.IdPointStart == conn.IdPointEnd)
                        continue;

                    var distance = CalculateDistanceBetweenPoints(
                        points.FirstOrDefault(i => i.IdPoint == conn.IdPointStart),
                        points.FirstOrDefault(i => i.IdPoint == conn.IdPointEnd));

                    int startIndex = StartPoints.IndexOf((int)conn.IdPointStart);
                    if (startIndex == -1)
                    {
                        startIndex = StartPoints.Count;
                        StartPoints.Add((int)conn.IdPointStart);
                        EndPoints[startIndex] = new List<Tuple<int, double>>();
                    }

                    EndPoints[startIndex].Add(new Tuple<int, double>((int)conn.IdPointEnd, distance));

                    int endIndex = StartPoints.IndexOf((int)conn.IdPointEnd);
                    if (endIndex == -1)
                    {
                        endIndex = StartPoints.Count;
                        StartPoints.Add((int)conn.IdPointEnd);
                        EndPoints[endIndex] = new List<Tuple<int, double>>();
                    }

                    EndPoints[endIndex].Add(new Tuple<int, double>((int)conn.IdPointStart, distance));
                }
            }
        }

        private static double CalculateDistanceBetweenPoints(MdlPoints pointStart, MdlPoints pointStop)
        {
            if (pointStart.X is null)
                return 0;
            if (pointStart.Y is null)
                return 0;
            if (pointStop.X is null)
                return 0;
            if (pointStop.Y is null)
                return 0;
            var xDiff = Math.Abs(Convert.ToDouble(pointStart.X - pointStop.X));
            var yDiff = Math.Abs(Convert.ToDouble(pointStart.Y - pointStop.Y));

            return Math.Sqrt(xDiff * xDiff + yDiff * yDiff);
        }

        public class NextPoint
        {
            public int IdPoint { get; set; }
            public int Icon { get; set; }
            public double Angle { get; set; }
            public double Distance { get; set; }
            public bool Elevator { get; set; }
            public bool Stairs { get; set; }
            public int Level { get; set; }
            public int CurrentLevel { get; set; }
            public int IconOnAnotherLevel { get; set; }
            public double AngleOnAnotherLevel { get; set; }
            public double DistanceOnAnotherLevel { get; set; }
        }
    }
}
