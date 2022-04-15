package com.example.a4starter

import android.graphics.Path
import android.graphics.PathMeasure
import kotlin.math.*

class PathSampler {
    companion object {
        private const val SAMPLING_RES = 128

        /**
         * @param path path to sample
         *
         * @return array of points corresponding
         * to sampling of the path
         */
        fun samplePath(path: Path) : ArrayList<Point> {
            val pathMeasure = PathMeasure(path, false)
            val points = ArrayList<Point>()

            val segLength = pathMeasure.length / SAMPLING_RES
            val coords = floatArrayOf(0f, 0f)

            for(i in 0 until SAMPLING_RES) {
                pathMeasure.getPosTan(i * segLength, coords, null)
                points.add(Point(coords[0], coords[1]))
            }

            return points
        }

        fun fitInSquare(points: ArrayList<Point>, center: Point, size: Float) : ArrayList<Point> {
            var minX = Float.MAX_VALUE
            var maxX = Float.MIN_VALUE
            var minY = Float.MAX_VALUE
            var maxY = Float.MIN_VALUE
            for(i in points.indices) {
                if(points[i].x < minX) {
                    minX = points[i].x
                } else if(points[i].x > maxX) {
                    maxX = points[i].x
                }
                if(points[i].y < minY) {
                    minY = points[i].y
                } else if(points[i].y > maxY) {
                    maxY = points[i].y
                }
            }

            val width = maxX - minX
            val height = maxY - minY

            // Scale
            val scale = size / max(width, height)
            //val scaleMatrix = FloatArray(4)
            //scaleMatrix[0] = scale
            for(i in points.indices) {
                points[i].x *= scale
                points[i].y *= scale
            }

            // Translate
            val centroid = centroid(points)
            for(i in points.indices) {
                points[i].x += (center.x - centroid.x)
                points[i].y += (center.y - centroid.y)
            }

            return points
        }

        private fun centroid(points: ArrayList<Point>) : Point {
            val n = points.size
            var x = 0f
            var y = 0f
            for(i in 0 until n) {
                x += points[i].x
                y += points[i].y
            }
            return Point(x / n, y / n)
        }

        fun rotateToZero(points: ArrayList<Point>): ArrayList<Point> {
            val centroid = centroid(points)
            val start = points[0]
            val angle = PI - atan((start.y - centroid.y) / (start.x - centroid.x))


            return rotateArray(points, angle, centroid)
            //val rotationMatrix = Matrix.

            //rotationMatrix.mapPoints(rotatedArray, 0, pointsArray, 0, points.size)
        }

        private fun rotateArray(array: ArrayList<Point>, angle: Double, axis: Point) : ArrayList<Point> {
            val rotationMatrix = FloatArray(4)
            rotationMatrix[0] = cos(angle).toFloat()
            rotationMatrix[1] = sin(angle).toFloat()
            rotationMatrix[2] = -sin(angle).toFloat()
            rotationMatrix[3] = cos(angle).toFloat()
            val rotatedPoints = ArrayList<Point>()

            for(i in 0 until array.size) {
                // Translate to axis
                array[i] = Point(array[i].x - axis.x, array[i].y - axis.y)

                // rotate array
                val rotatedX = rotationMatrix[0] * array[i].x + rotationMatrix[2] * array[i].y
                val rotatedY = rotationMatrix[1] * array[i].x + rotationMatrix[3] * array[i].y

                // Translate back to the original translation
                val x = rotatedX + axis.x
                val y = rotatedY + axis.y
                rotatedPoints.add(Point(x, y))
            }

            return rotatedPoints
        }
    }
}