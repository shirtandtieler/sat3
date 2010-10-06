package com.anjlab.sat3;

import java.util.Iterator;

public class SimpleTier extends SimpleTripletPermutation implements ITier
{
    protected int keys_73516240;
    private int size;
    
    public static SimpleTier createCompleteTier(int a, int b, int c)
    {
        SimpleTier result = new SimpleTier(a, b, c);
        result.keys_73516240 = 255;
        result.size = 8;
        return result;
    }
    
    public SimpleTier(int a, int b, int c)
    {
        super(a, b, c);
    }

    public SimpleTier(ITripletPermutation tripletPermutation)
    {
        this(tripletPermutation.getAName(), 
             tripletPermutation.getBName(),
             tripletPermutation.getCName());
    }

    public boolean add(ITripletValue triplet)
    {
        if (!contains(triplet)) //    We need this check to keep value of size correct 
        {
            size++;
            keys_73516240 = keys_73516240 | triplet.getTierKey();
            return true;
        }
        else
        {
            return false;
        }
    }

    public int size()
    {
        return size;
    }

    public boolean contains(ITripletValue triplet)
    {
        int key = triplet.getTierKey();
        return (keys_73516240 & key) == key;
    }

    public void remove(ITripletValue triplet)
    {
        if (contains(triplet))    //    We need this check to keep value of size correct
        {
            removeKey(triplet.getTierKey());
        }
    }

    private void removeKey(int key) {
        keys_73516240 = keys_73516240 & (255 ^ key);
        size--;
    }

    /**
     * Use only if performance is not a goal.
     */
    public Iterator<ITripletValue> iterator()
    {
        return new Iterator<ITripletValue>()
        {
            private int key = 0;
            private byte counter = 0;
            public boolean hasNext()
            {
                return counter < size;
            }
            public ITripletValue next()
            {
                key = key == 0 ? 1 : key << 1;
                boolean hasValue = (keys_73516240 & key) == key;
                while (!hasValue)
                {
                    key <<= 1;
                    hasValue = (keys_73516240 & key) == key;
                }
                
                counter++;
                return SimpleTripletValueFactory.getTripletValue(key);
            }
            public void remove()
            {
                removeKey(key);
            }
        };
    }

    public final void swapAB()
    {
        super.swapAB();
        
        if (size == 0)
        {
            return;
        }
        
        int keys_oo51oooo = keys_73516240 & 0x30;
        int keys_oooo62oo = keys_73516240 & 0x0C;
        
        keys_73516240 = (keys_73516240 & 0xC3)
                      | (keys_oo51oooo >> 2)
                      | (keys_oooo62oo << 2);
    }
    
    public final void swapAC()
    {
        super.swapAC();
        
        if (size == 0)
        {
            return;
        }
        
        int keys_o3o1oooo = keys_73516240 & 0x50;
        int keys_oooo6o4o = keys_73516240 & 0x0A;
        
        keys_73516240 = (keys_73516240 & 0xA5)
                      | (keys_o3o1oooo >> 3)
                      | (keys_oooo6o4o << 3);
    }

    public final void swapBC()
    {
        super.swapBC();
        
        if (size == 0)
        {
            return;
        }
        
        int keys_o3ooo2oo = keys_73516240 & 0x44;
        int keys_oo5ooo4o = keys_73516240 & 0x22;
        
        keys_73516240 = (keys_73516240 & 0x99)
                      | (keys_o3ooo2oo >> 1)
                      | (keys_oo5ooo4o << 1);
    }
    
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getAName());
        builder.append(",");
        builder.append(getBName());
        builder.append(",");
        builder.append(getCName());
        return builder.toString();
    }

    public void subtract(ITier tier)
    {
        if (size != 8)
            throw new UnsupportedOperationException("Operation implemented only when subtracting from complete tier");
        
        if (canonicalHashCode() != tier.canonicalHashCode())
            throw new IllegalArgumentException("Cannot subtract tiers with different set of variables: " + this + " and " + tier);
        
        size -= tier.size();
        keys_73516240 = keys_73516240 ^ ((SimpleTier) tier).keys_73516240;
    }

    public void adjoinRight(ITier tier)
    {
        int tier_keys_73516240 = ((SimpleTier) tier).keys_73516240;
        
        int this_keys_o6o2o4o0 = ((tier_keys_73516240 >> 1) | tier_keys_73516240) & 0x55;
        int this_keys_7o3o5o1o = ((this_keys_o6o2o4o0 << 1));
        
        int this_keys_76325410 = (this_keys_o6o2o4o0 | this_keys_7o3o5o1o) & get_keys_76325410();
        
        keys_73516240 = get_keys_73516240_from(this_keys_76325410);
        
        updateSize();
    }

    public void adjoinLeft(ITier tier)
    {
        int tier_keys_76325410 = ((SimpleTier) tier).get_keys_76325410();
        
//        printBits(((SimpleTier) tier).keys_73516240);
//        printBits(tier_keys_76325410);
        
        int this_keys_o3o1o2o0 = ((tier_keys_76325410 >> 1) | tier_keys_76325410) & 0x55;
        int this_keys_7o5o6o4o = ((this_keys_o3o1o2o0 << 1));
        
//        printBits(this_keys_o3o1o2o0);
//        printBits(this_keys_7o5o6o4o);
        
//        printBits(keys_73516240);
        
        keys_73516240 = (this_keys_7o5o6o4o | this_keys_o3o1o2o0) & keys_73516240;
        
//        printBits(keys_73516240);
        
        updateSize();
    }
    
    private void updateSize()
    {
        size = 0;
        
        int mask = 1;
        for (int i = 0; i < 8; i++)
        {
            if ((keys_73516240 & mask) == mask)
            {
                size++;
            }
            mask <<= 1;
        }
    }

    private int get_keys_73516240_from(int keys_76325410)
    {
        int keys_7o3o5o1o = keys_76325410;
        int keys_6o2o4o0o = keys_76325410 << 1;
        
        int keys_7351oooo = 0;
        int keys_6240oooo = 0;
        
        int mask          = 0x80;
        
        for (int i = 0; i < 4; i++)
        {
            keys_7351oooo = (keys_7351oooo)
                          | (keys_7o3o5o1o & mask);
            
            keys_6240oooo = (keys_6240oooo)
                          | (keys_6o2o4o0o & mask);
                          
            keys_7o3o5o1o <<= 1;
            keys_6o2o4o0o <<= 1;
            mask          >>= 1;

        }
        
        return keys_7351oooo | (keys_6240oooo >> 4);
    }

    private int get_keys_76325410()
    {
        int keys_7351oooo = (keys_73516240 & 0xF0);
        int keys_o6420ooo = (keys_73516240 & 0x0F) << 3;
        int mask          = 0x80;

//        printBits(keys_73516240);

        int keys_76325410 = 0;
        
        for (int i = 0; i < 8; i++)
        {
//            printBits(keys_7351oooo);
//            printBits(keys_o6420ooo);

            keys_76325410 = (keys_76325410)
                          | (keys_7351oooo & (mask))
                          | (keys_o6420ooo & (mask >>= 1));

//            printBits(keys_76325410);

            keys_7351oooo >>= 1;
            keys_o6420ooo >>= 1;
            mask          >>= 1;
        }
        
        return keys_76325410;
    }

    public void union(ITier tier)
    {
        keys_73516240 = keys_73516240 | ((SimpleTier) tier).keys_73516240;
        
        updateSize();
    }

//    private void printBits(int keys)
//    {
//        int mask = 0x80;
//        while (mask > 0)
//        {
//            if ((keys & mask) == mask)
//            {
//                System.out.print("1");
//            }
//            else
//            {
//                System.out.print("0");
//            }
//            mask >>= 1;
//        }
//        System.out.println();
//    }
}