import org.omg.SendingContext.RunTime;

/**
 * Created by Jordan on 15-May-17.
 */
public class PA3
{

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



        // TODO: Read m, n, qMax from args[]
        // TODO: Check that args[] has enough supplied values. If not enough  values, throw error and end

        Simulation simulation = new Simulation(m,n,qMax);

        simulation.startProcessing();
        simulation.runDataStatistics();

    }
}
