/**
 * public class PA3  - Entry point of the program
 */

import java.text.NumberFormat;
import java.util.Random;

public class PA3
{
    /**
     * public static void main(String[]args)
     * Program expects three input parameters (Double, Double, Int)
     * Doubles must be valid
     * Integer value must be greater than 1
     * @param args - String arguments from the command line
     */
    public static void main(String[]args)
    {
        //long startTime = System.currentTimeMillis();

        if(args.length < 3)
        {
            System.out.println("The three arguments were not provided correctly: [M N QMAX]");
            System.exit(0);
        }

        double m, n;
        int qMax;

        try
        {
            m = Double.parseDouble(args[0]);
            n = Double.parseDouble(args[1]);
            qMax = Integer.parseInt(args[2]);
            if(qMax <= 1)
                throw new RuntimeException("QMAX is less than or equal to 1");

        }
        catch(NumberFormatException e)
        {
            throw new NumberFormatException("Input for one or more arguments is not in the correct format [Double, Dobule, Int]");
        }


        //Random r = new Random();
        Simulation simulation = new Simulation(m,n,qMax);

        simulation.startProcessing();
        simulation.runDataStatistics();

       /* long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);*/

    }
}
