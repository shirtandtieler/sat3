package com.anjlab.sat3;

import static com.anjlab.sat3.Helper.printFormulas;

public class Program
{
    public static void main(String[] args) throws Exception
    {
        if (args.length != 1)
        {
            System.out.println("Usage:\n\tjava [-Dverbose=true] " + Program.class.getName() + " formula.cnf");
            System.exit(0);
        }

        String verbose = System.getProperty("verbose");
        
        Helper.UsePrettyPrint = verbose != null && verbose.equalsIgnoreCase("true");
        Helper.EnableAssertions = true;
        Helper.UseUniversalVarNames = true;

        StopWatch stopWatch = new StopWatch();
        
        try
        {
            stopWatch.start("Load formula");
            ITabularFormula formula = Helper.loadFromGenericDIMACSFileFormat(args[0]);
            stopWatch.stop();
            
            Helper.prettyPrint(formula);
    
            stopWatch.printElapsed();
    
            stopWatch.start("Create CTF");
            GenericArrayList<ITabularFormula> ct = Helper.createCTF(formula);
            stopWatch.stop();
    
            printFormulas(ct);
    
            System.out.println("CTF: " + ct.size());
    
            stopWatch.printElapsed();
    
            stopWatch.start("Create CTS");
            Helper.createCTS(formula, ct);
            stopWatch.stop();
    
            formula = null;
            
            printFormulas(ct);
    
            stopWatch.printElapsed();
    
    //        saveCTS(args[0], ct);
            
            stopWatch.start("Unify all CTS");
            Helper.unify(ct);
            stopWatch.stop();
            
            printFormulas(ct);
            
            stopWatch.printElapsed();

            System.out.println("CTF: " + ct.size());
        }
        catch (EmptyStructureException e)
        {
            stopWatch.stop();
            stopWatch.printElapsed();
            
            System.out.println("Formula not satisfiable, because one of the structures was built empty");
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Program completed");
        }
    }

}
