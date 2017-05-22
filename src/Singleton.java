/**
 * Singleton Class
 * Utilises a private constructor and appropriate methods to create a single instance of an object
 */
//Implementation for Singleton found and adapted from:
//http://www.javaworld.com/article/2073352/core-java/simply-singleton.html?page=1
public class Singleton
{
    private static Singleton instance = null;
    private static int nextID = 0;


    /**
     * private Singleton()
     * Only purpose is to defeat instantiation
     */
    private Singleton()
    {
        // Exists only to defeat instantiation.
    }

    /**
     * public static Singleton getInstance()
     * Returns the Instance Singleton Object if/ if not created
     * @return - Singleton Instance private object
     */
    public static Singleton getInstance()
    {
        if(instance == null)
            instance = new Singleton();

        return instance;
    }

    /**
     *  public int getNextID()
     *  @return - Returns an incremented ID value
     */
    public int getNextID()
    {
        return ++nextID;
    }

}

