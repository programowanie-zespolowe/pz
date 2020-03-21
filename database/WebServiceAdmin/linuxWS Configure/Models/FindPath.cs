using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using linuxWS_Configure.Models;

namespace WhereToGo.Admin.Models
{
    public class FindPath
    {
        //Build id 30
        //Point 166, 167, 168, 169
        public static NextPoint GetNextPoint(int? idPrevPoint, int idActualPoint, int idDestPoint, List<MdlPoints> points, List<MdlPointsConnection> connections)
        {
            var adjList = new AdjacencyList(points, connections);
            var nextPoint = new NextPoint();
            var adjListPointId = (int)Dijkstra(adjList.StartPoints.IndexOf(idActualPoint), adjList.StartPoints.IndexOf(idDestPoint), adjList);

            var actualPoint = points.Where(i => i.IdPoint == idActualPoint).FirstOrDefault();
            var destPoint = points.Where(i => i.IdPoint == idDestPoint).FirstOrDefault();

            nextPoint.IdPoint = adjList.StartPoints[adjListPointId];
            nextPoint.Distance = CalculateDistanceBetweenPoints(
                actualPoint,
                points.Where(i => i.IdPoint == nextPoint.IdPoint).FirstOrDefault());

            if (idPrevPoint is null || (bool)points.Where(i => i.IdPoint == idActualPoint).FirstOrDefault().OnOffDirection)
            {
                var prevPoint = CalculateVirtualPoint(actualPoint);
                nextPoint.Angle = Angle((double)actualPoint.X, (double)actualPoint.Y, (double)prevPoint.X, (double)prevPoint.Y, (double)destPoint.X, (double)destPoint.Y);
            }
            else
            {
                var prevPoint = points.Where(i => i.IdPoint == idPrevPoint).FirstOrDefault();
                nextPoint.Angle = Angle((double)actualPoint.X, (double)actualPoint.Y, (double)prevPoint.X , (double)prevPoint.Y, (double)destPoint.X, (double)destPoint.Y);
            }

            return nextPoint;
        }

        private static MdlPoints CalculateVirtualPoint(MdlPoints point)
        {
            double direction = Math.PI * (!(point.Direction is null) ? (double)point.Direction : 0) / 180.0;

            var returnPoint = new MdlPoints();
            const double DISTANCE = 10.0;
            var diffX = DISTANCE * Math.Sin(direction);
            var diffY = DISTANCE * Math.Cos(direction);

            returnPoint.X = point.X - diffX;
            returnPoint.Y = point.Y - diffY;

            return returnPoint;
        }

        private static int? Dijkstra(int startPointId, int endPointId, AdjacencyList list)
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
                        prev[i] = num;
                    }
                }
            }

            return DijkstraNextPoint(startPointId, endPointId, distance, prev);
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

        private static double Angle(double x1, double y1, double x2, double y2, double x3, double y3)
        {
            double numerator = y2 * (x1 - x3) + y1 * (x3 - x2) + y3 * (x2 - x1);
            double denominator = (x2 - x1) * (x1 - x3) + (y2 - y1) * (y1 - y3);
            double ratio = numerator / denominator;
            double angleRad = Math.Atan(ratio);
            double angleDeg = (angleRad * 180) / Math.PI;

            if (angleDeg < 0)
            {
                angleDeg = 180 + angleDeg;
            }
            return angleDeg;
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
                        points.Where(i => i.IdPoint == conn.IdPointStart).FirstOrDefault(),
                        points.Where(i => i.IdPoint == conn.IdPointEnd).FirstOrDefault());

                    int startIndex = StartPoints.IndexOf((int) conn.IdPointStart);
                    if (startIndex == -1)
                    {
                        startIndex = StartPoints.Count;
                        StartPoints.Add((int) conn.IdPointStart);
                        EndPoints[startIndex] = new List<Tuple<int, double>>();
                    }

                    EndPoints[startIndex].Add(new Tuple<int, double>((int) conn.IdPointEnd, distance));

                    int endIndex = StartPoints.IndexOf((int) conn.IdPointEnd);
                    if (endIndex == -1)
                    {
                        endIndex = StartPoints.Count;
                        StartPoints.Add((int) conn.IdPointEnd);
                        EndPoints[endIndex] = new List<Tuple<int, double>>();
                    }

                    EndPoints[endIndex].Add(new Tuple<int, double>((int) conn.IdPointStart, distance));
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
            public int IdPoint { get;  set; }
            public int Icon { get;  set; }
            public double Angle { get; set; }
            public double Distance { get; set; }
        }
    }
}
