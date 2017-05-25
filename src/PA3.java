/**
 * public class PA3  - Entry point of the program
 */
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
        double m, n;
        int qMax;

        if(args[0] != null)
            m = Double.parseDouble(args[0]);
        else
            throw new RuntimeException("M Value has not been entered correctly");

        if(args[1] != null)
            n = Double.parseDouble(args[1]);
        else
            throw new RuntimeException("N Value has not been entered correctly");

        if(args[2] != null)
        {
            qMax = Integer.parseInt(args[2]);
            if(qMax <= 1)
                throw new RuntimeException("QMAX is less than 1");
        }
        else
            throw new RuntimeException("QMAX Value has not been entered correctly");


        Simulation simulation = new Simulation(m,n,qMax);

        simulation.startProcessing();
        simulation.runDataStatistics();

    }
}
