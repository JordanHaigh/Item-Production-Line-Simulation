/**
 * Created by Jordan on 15-May-17.
 */
public class PA3
{

    public static void main(String[]args)
    {
        // TODO: Read m, n, qMax from args[]
        // TODO: Check that args[] has enough supplied values. If not enough  values, throw error and end

        Simulation simulation = new Simulation(1000,1000,7);

        simulation.startProcessing();
        simulation.runDataStatistics();

    }
}
