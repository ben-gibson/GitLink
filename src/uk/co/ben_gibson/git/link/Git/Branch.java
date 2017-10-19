package uk.co.ben_gibson.git.link.Git;

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

    public Boolean equals(Branch branch)
    {
        return this.toString().equals(branch.toString());
    }
}
