public class Program
{

	public static void main(String[] args)
	{
		//Start algorythm with parameters:
		//(int popSize, int popCount, String filename, int crossProb, int mutProb)
		GeneticAlgorythm ga = new GeneticAlgorythm(300, 60, "D:\\SI\\lab1\\had14.dat", 60, 10);
		ga.getResults();
	}

}
