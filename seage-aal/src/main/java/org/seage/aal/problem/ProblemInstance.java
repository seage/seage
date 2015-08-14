package org.seage.aal.problem;

public abstract class ProblemInstance
{
    protected ProblemInstanceInfo _problemInstanceInfo;

    public ProblemInstance(ProblemInstanceInfo problemInstanceInfo)
    {
        super();
        _problemInstanceInfo = problemInstanceInfo;
    }

    public ProblemInstanceInfo getProblemInstanceInfo()
    {
        return _problemInstanceInfo;
    }

}
