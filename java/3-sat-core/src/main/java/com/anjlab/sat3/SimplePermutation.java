package com.anjlab.sat3;

import java.util.Arrays;

import cern.colt.list.IntArrayList;
import cern.colt.map.OpenIntIntHashMap;

public final class SimplePermutation implements IPermutation
{
    private final IntArrayList permutation;
    private final OpenIntIntHashMap permutationHash;
    private final OpenIntIntHashMap positionHash;
    
    public SimplePermutation()
    {
        permutation = new IntArrayList();
        permutationHash = new OpenIntIntHashMap();
        positionHash = new OpenIntIntHashMap();
    }

    public final boolean contains(int varName)
    {
        //    O(1)
        return permutationHash.containsKey(varName);
    }

    public final int indexOf(int varName)
    {
        //  TODO Replacing zero-based indexing with the 1-based
        //  allow remove invocation of containsKey method here
        
        //    O(1)
        return permutationHash.containsKey(varName)
             ? permutationHash.get(varName)
             : -1;
    }

    public final void add(int varName)
    {
        if (Helper.EnableAssertions)
        {
            assertNotContains(varName);
        }
        
        permutation.add(varName);
        
        int index = permutation.size() - 1;
        
        permutationHash.put(varName, index);
        positionHash.put(index, varName);
    }

    /**
     * Insert <code>varName</code> before position <code>index</code>.
     * 
     * Note: This implementation can be slow. To avoid usage of this method remove 
     * the <code>JoinBetweenTiers2</code> implementation of {@link IJoinMethod} from {@link JoinMethods#getMethods()}.
     */
    public final void add(int index, int varName)
    {
        if (Helper.EnableAssertions)
        {
            assertNotContains(varName);
        }
        
        permutation.beforeInsert(index, varName);
        permutationHash.put(varName, index);
        
        for (int i = permutation.size() - 2; i >= index; i--)
        {
            int var = positionHash.get(i);
            int idx = permutationHash.get(var);
            permutationHash.put(var, idx + 1);
            positionHash.put(i + 1, var);
        }
        
        positionHash.put(index, varName);
    }

    public final void shiftToStart(int from, int to)
    {
        if (to <= from)
        {
            throw new IllegalArgumentException("to <= from");
        }
        int[] buffer = new int[from];   //  Allocate single static buffer somewhere outside?
        
        int[] elements = permutation.elements();
        
        System.arraycopy(elements, 0, buffer, 0, from);
        System.arraycopy(elements, from, elements, 0, to - from + 1);
        System.arraycopy(buffer, 0, elements, to - from + 1, from);
        
        for (int i = 0; i <= to; i++)
        {
            int varName = elements[i];
            permutationHash.put(varName, i);
            positionHash.put(i, varName);
        }
    }
    
    public final void shiftToEnd(int from, int to)
    {
        if (to <= from)
        {
            throw new IllegalArgumentException("to <= from");
        }
        int[] buffer = new int[permutation.size() - to - 1];   //  Allocate single static buffer somewhere outside?
        
        int[] elements = permutation.elements();
        
        System.arraycopy(elements, to + 1, buffer, 0, permutation.size() - to - 1);
        System.arraycopy(elements, from, elements, permutation.size() - (to - from) - 1, to - from + 1);
        System.arraycopy(buffer, 0, elements, from, permutation.size() - to - 1);
        
        for (int i = from; i < permutation.size(); i++)
        {
            int varName = elements[i];
            permutationHash.put(varName, i);
            positionHash.put(i, varName);
        }
    }
    
    public void swap(int varName1, int varName2)
    {
        int index1 = permutation.indexOf(varName1);
        int index2 = permutation.indexOf(varName2);
        
        positionHash.put(index1, varName2);
        positionHash.put(index2, varName1);
        
        permutationHash.put(varName1, index2);
        permutationHash.put(varName2, index1);
        
        permutation.setQuick(index1, varName2);
        permutation.setQuick(index2, varName1);
    }
    
    private final void assertNotContains(int varName)
    {
        if (contains(varName))
        {
            throw new IllegalStateException("Permutation " + this + " already contains variable " + varName);
        }
    }

    public final int size()
    {
        return permutation.size();
    }

    public final int[] elements()
    {
        return permutation.elements();
    }
    
    public final int get(int index)
    {
        return permutation.get(index);
    }

    public final String toString()
    {
        return permutation.toString();
    }
    
    public final boolean sameAs(IPermutation permutation)
    {
        return Arrays.equals(
                this.permutation.elements(),
                ((SimplePermutation) permutation).permutation.elements());
    }

    public static IPermutation createPermutation(int... variables)
    {
        IPermutation permutation = new SimplePermutation();
        
        for (int varName : variables)
        {
            permutation.add(varName);
        }
        
        return permutation;
    }
}
