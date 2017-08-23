package uk.co.ben_gibson.open.in.git.host.Git;

public class Branch
{
    private String name;

    public Branch(String name)
    {
        this.name = name;
    }

    public static Branch master()
    {
        return new Branch("master");
    }

    public String toString()
    {
        return this.name;
    }
}
