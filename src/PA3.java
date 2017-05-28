/**
 * public class PA3  - Entry point of the program
 */

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

        //Variables initialised only to prevent catching the wrong exceptions
        double m = 0;
        double n = 0;
        int qMax = 0;

        try
        {
            m = Double.parseDouble(args[0]);
            if(m < 0)
                throw new RuntimeException("M must be positive");
            n = Double.parseDouble(args[1]);
            qMax = Integer.parseInt(args[2]);
            if(qMax <= 1)
                throw new RuntimeException("QMAX is less than or equal to 1");
        }
        catch(Exception e)
        {
            System.err.println("Input for one or more arguments is not in the correct format.\n" + e.getMessage());
            System.exit(1);
        }

        Random r = new Random();
        //Random r = new Random();
        Simulation simulation = new Simulation(m,n,qMax, r);
        simulation.startProcessing();
        simulation.runDataStatistics();

        /*long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);*/

    }
}
