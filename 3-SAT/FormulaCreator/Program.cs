﻿using System;
using System.Collections.Generic;
using Microsoft.Contracts;

namespace FormulaCreator
{
    public class Program
    {
        public static void Main(string[] args)
        {
//            var formula = Helper.LoadFromFile("article-example.csv");
//
            const int seed = 42;
            var rand = new Random(seed);
//
//            const int maxN = 26;
//            var n = rand.Next(3, maxN + 1);
//
//            var mMax = 8*8*(n - 2);
//            var m = rand.Next(1, mMax + 1);
//
            var n = 560;
            var m = 2380;

            var formula = Helper.CreateRandomFormula(rand, n, m);

            var filename = string.Format("{0}-{1}-{2}.csv", n, m, seed);
            Helper.SaveToFile(formula, filename);

//
//            formula = Helper.LoadFromFile(filename);

            Helper.PrettyPrint(formula);

            Console.WriteLine(new string('*', 70));

            List<ITabularFormula> ctf = Helper.CreateCTF(formula);

            foreach (var f in ctf)
            {
                f.SortTiers();

                Helper.PrettyPrint(f);
            }

            Console.WriteLine(new string('*', 70));

            var cts = Helper.CreateCTS(formula, ctf);

            foreach (var f in cts)
            {
                Helper.PrettyPrint(f);
            }

            Console.WriteLine("CTF: " + ctf.Count);

            Console.ReadLine();
        }
    }
}
