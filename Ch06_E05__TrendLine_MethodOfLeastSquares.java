/*
   Author: Mike O'Malley
   Description: Trend Line by Method of Least Squares
   My solution to the problem in Chapter 6, Exercises 6.5 and 6.6, pp182-187.

   Structured Fortran 77 for Engineers and Scientists,
   D. M. Etter.
   (C) 1983.  ISBN: 0-8053-2520-4

My old QIT (Uni) textbook from my uni days 1983-1987 - VERY weather beaten and worn now (almost 30 years later).

* Chapter 6, Exercises 6.5 and 6.6, pp182-187, Structured Fortran 77 for Engineers and Scientists, D. M. Etter. (C) 1983.  ISBN: 0-8053-2520-4

I have rewritten the question and discussion below to make it a lot clearer and more focused,
and suitable for use in tutorial questions or perhaps even an Assignment for students.

********************************************
Questions:
********************************************

The following data represents the load-deflection of a coil spring,
where:
* Load is the weight applied to the spring in Kg, and
* Length is the length of the spring in cm after the Load has been applied to the spring.

 Load   Length
------  ------
 0.28    6.62
 0.50    5.93
 0.67    4.46
 0.93    4.25
 1.15    3.30
 1.38    3.15
 1.60    2.43
 1.98    1.46

Example: for a load 0.28 Kg, the length of the spring is 6.62 cm.

As the load increases, the length of the spring decreases (because the spring is compressed and forced down).


(1). Store the above data in a suitable array and calculate and display the
equation of the "line of best fit" (aka "trend line").

The "Method of Least Squares" is a standard technique for determining the equation of a
straight line from a set of data, also called the "line of best fit" and "trend line".

Recall that the equation of a straight line is:

   Y = M X + C

where M is the slope (or gradient) of the line and C is the Y intercept (where the line crosses the Y axis).

The slope and Y intercept for the "line of best fit" (aka "trend line")
can be calculated from data using the following equations:

sumX  = X1 + X2 + ... + Xn
sumY  = Y1 + Y2 + ... + Yn
sumXY = X1 * Y1 + X2 * Y2 + ... + Xn * Yn
sumXX = X1 * X1 + X2 * X2 + ... + Xn * Xn

slope = (sumX * sumY - n * sumXY) / (sumX * sumX - n * sumXX);

Y intercept = (sumY - slope * sumX) / n;

(n is the number of data points)


(2). If the data points and trend line were plotted on a piece of graph paper,
or using computer software like a spreadsheet, it would be possible to see
how closely the line fitted the data.  Another way to see how well the line
fits the data is the take an X value and plug it into the "trend line" equation,
thus calculating a Y value.  This new Y value, designated Y', would be an
estimate of the value of Y for a given X value.

For example, suppose the least squares technique yielded the following trend
line equation:

Y' = 4.2 X - 3.1

For a given data point (1.0, 0.9) (i.e. X = 1.0, Y = 0.9) the estimated
(calculated) Y value is:

Y' = 4.2 * 1.0 - 3.1 = 1.1

Display all points in the arrray, and for each point calculate and display the
the estimated (calculated) Y value, Y', using the trend line equation.


(3). The residual for a data point is the difference between the actual value of Y and the
estimated value of Y:

residual = Y - Y'

The residual for the data point above is 0.9 - 1.1 = -0.2.

The sum the squares of the residuals for all data points is called the Residual Sum,
and this provides an estimate of the quality of fit of the trend line to the
actual data, without needing to plot the actual data.

When the trend line is a perfect fit for the data (the data is perfectly linear), then the
Residual Sum will be zero.  As Residual Sum increases, the less the data can be estimated
by the trend line.

Calculate and display the Residual for all data points and the Residual Sum.

(4). Now that we have out trend line equation and a measure of how good the line is,
we can use the trend line to make estimates and fill in gaps.

Use your trend line to answer the following questions:
* The natural length of the spring (i.e. the length when there is no load).
* The load when Spring is 5 cm long


References:
* The above questions are based on exerises in my old Fortran text book:

   Structured Fortran 77 for Engineers and Scientists,
   D. M. Etter, (C) 1983, ISBN: 0-8053-2520-4.
   Chapter 6, Exercises 6.5 and 6.6, pp182-187.

*/

/*
********************************************
Sample output:
********************************************

Trend Line by Method of Least Squares - by Mike O'Malley

Trend Line Equation:  (where Y is Length and X is Load)

   Y = -2.93 X +  7.06

Load    Length  Length    Length
(X)      (Y)     (Y)       (Y)
Actual  Actual  Estimate  Residual
------  ------  --------  --------
 0.28    6.62     6.24      0.38
 0.50    5.93     5.59      0.34
 0.67    4.46     5.10     -0.64
 0.93    4.25     4.33     -0.08
 1.15    3.30     3.69     -0.39
 1.38    3.15     3.02      0.13
 1.60    2.43     2.37      0.06
 1.98    1.46     1.26      0.20

Residualual Sum (Error) =  0.88

Estimates based on Trend Line:
* Length of Spring (no load)         =  7.06
* Length of Spring with 2.1 Kg load  =  0.91
* Load when Spring is 5 cm long      =  0.70


*/

import java.awt.Point; // for integer points.
import java.awt.geom.Point2D.Double;

public class Ch06_E05__TrendLine_MethodOfLeastSquares
{
   // Load Deflection of a Coil Spring
   // x = Load, y = deflection.
   // e.g. for load 0.28 Kg, the deflection is 6.62 cm, so the 1st point in the array is (0.28, 6.62).
   private static final double valuesArray [][] = {{0.28, 0.50, 0.67, 0.93, 1.15, 1.38, 1.60, 1.98},
                                                   {6.62, 5.93, 4.46, 4.25, 3.30, 3.15, 2.43, 1.46}};

   private static final String outputFormat = "% ,.2f"; // comma separator, leading space for positive values, and 2 decimal places.

   public static Double CalcLeastSquaresTrendLineEquation (double valuesArray [][])
   {
      double sumX           = 0.0;
      double sumY           = 0.0;
      double sumXY          = 0.0;
      double sumXX          = 0.0;
      double lineGradient   = 0.0;
      double lineYIntercept = 0.0;
      int numPoints         = valuesArray[0].length;

      for (int k = 0; k < numPoints; k++)
      {
         //System.out.println ("(" + k + ", " + k + ") = " + valuesArray [0][k] + ", " + valuesArray [1][k]);

         sumX  = sumX  + valuesArray [0][k];
         sumXX = sumXX + valuesArray [0][k] * valuesArray [0][k];
         sumY  = sumY  + valuesArray [1][k];
         sumXY = sumXY + valuesArray [0][k] * valuesArray [1][k];
      }

      lineGradient   = (sumX * sumY - numPoints * sumXY) / (sumX * sumX - numPoints * sumXX);
      lineYIntercept = (sumY - lineGradient * sumX) / numPoints;

      // Y = M * X + C
      // Use a (X, Y) point to store this, where x = gradient, and y = C (the constant = Y Intercept)
      return new Double (lineGradient, lineYIntercept);
   }

   public static double CalcYValue (Double trendLineEqu, double x)
   {
      // Y = M * X + C
      // Use a (X, Y) point to store this, where x = gradient, and y = C (the constant = Y Intercept)
      return trendLineEqu.getX() * x + trendLineEqu.getY();
   }

   public static double CalcXValue (Double trendLineEqu, double y)
   {
      // Y = M * X + C
      // => X = (Y - C) / M
      // Use a (X, Y) point to store this, where x = gradient, and y = C (the constant = Y Intercept)
      return (y - trendLineEqu.getY()) / trendLineEqu.getX();
   }

   public static double CalcResidual (double valuesArray [][], Double trendLineEqu, int index)
   {
      // Calculate the error for a single point on the trend line VS the real data.
      // Residual = Y - Y'
      return valuesArray [1][index] - CalcYValue (trendLineEqu, valuesArray [0][index]);
   }

   public static double CalcSumResidual (double valuesArray [][], Double trendLineEqu)
   {
      // This is a measure of the error in the trend line values VS the real data.
      double residual       = 0.0;
      double sumResidualSqd = 0.0;
      int numPoints         = valuesArray[0].length;

      for (int k = 0; k < numPoints; k++)
      {
         //System.out.println ("(" + k + ", " + k + ") = " + valuesArray [0][k] + ", " + valuesArray [1][k]);

         // Residual = Y - Y'
         residual = valuesArray [1][k] - CalcYValue (trendLineEqu, valuesArray [0][k]);
         // OR:
         //CalcResidual (valuesArray, trendLineEqu, k);

         sumResidualSqd = sumResidualSqd + residual  * residual;
      }

      return sumResidualSqd;
   }


   public static void main (String [] args)
   {
      int numPoints         = valuesArray[0].length;
      Double trendLineEqu   = CalcLeastSquaresTrendLineEquation (valuesArray);

      System.out.println ();
      System.out.println ("Trend Line by Method of Least Squares - by Mike O'Malley");

      // Display the Trend Line Equation: Y = M X + C
      System.out.println ();
      System.out.println ("Trend Line Equation:  (where Y is Length and X is Load)");
      System.out.println ();
      System.out.println ("   Y = "  + String.format (outputFormat, trendLineEqu.getX()) +
                          " X + "    + String.format (outputFormat, trendLineEqu.getY()));

      // Display the original array data:
/*
      System.out.println ();
      System.out.println (" Load   Length");
      System.out.println ("------  ------");

      for (int k = 0; k < numPoints; k++)
      {
         System.out.println (String.format (outputFormat, valuesArray [0][k]) + "   " +
                             String.format (outputFormat, valuesArray [1][k]));
      }
*/

      // Display the Origina Data Vs calculations based on the Trend Line
      System.out.println ();
      System.out.println ("Load    Length  Length    Length  ");
      System.out.println ("(X)      (Y)     (Y)       (Y)    ");
      System.out.println ("Actual  Actual  Estimate  Residual");
      System.out.println ("------  ------  --------  --------");

      for (int k = 0; k < numPoints; k++)
      {
         System.out.println (String.format (outputFormat, valuesArray [0][k]) + "   " +
                             String.format (outputFormat, valuesArray [1][k]) + "    " +
                             String.format (outputFormat, CalcYValue (trendLineEqu, valuesArray [0][k])) + "     " +
                             String.format (outputFormat, CalcResidual (valuesArray, trendLineEqu, k)));
      }

      System.out.println ();
      System.out.println ("Residualual Sum (Error) = " + String.format (outputFormat, CalcSumResidual (valuesArray, trendLineEqu)));


      System.out.println ();
      System.out.println ("Estimates based on Trend Line:");
      System.out.println ("* Length of Spring (no load)         = " + String.format (outputFormat, CalcYValue (trendLineEqu, 0.0)));
      System.out.println ("* Length of Spring with 2.1 Kg load  = " + String.format (outputFormat, CalcYValue (trendLineEqu, 2.1)));
      System.out.println ("* Load when Spring is 5 cm long      = " + String.format (outputFormat, CalcXValue (trendLineEqu, 5.0)));
   }
}