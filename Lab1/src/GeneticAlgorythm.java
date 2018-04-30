import java.io.File;
import java.util.Scanner;

public class GeneticAlgorythm
{

	private int popSize;
	private int popCount;
	private int crossProbability;
	private int mutateProbability;
	private NumberPopulation[] populations;
	private int genSize;
	private int[][] distance;
	private int[][] flow;
	private int ratio;
		
	//constructor for whole algorythm calculation
	public GeneticAlgorythm (int popSize, int popCount, String filename, int crossProb, int mutProb)
	{
		this.popCount=popCount;
		this.popSize=popSize;
		crossProbability = crossProb;
		mutateProbability = mutProb;
		loadFile(filename);
		calcRatio();
		generatePopulations();
	}
	
	//initialize genotype size and flow/distance matrixes
	private void loadFile(String filename)
	{
		try
		{
			File file = new File (filename);
			Scanner sc = new Scanner (file);
			genSize = sc.nextInt();
			flow = new int[genSize][genSize];
			for (int i=0; i<genSize; i++)
				for (int j=0; j<genSize; j++)
					flow[i][j] = sc.nextInt();
			distance = new int[genSize][genSize];
			for (int i=0; i<genSize; i++)
				for (int j=0; j<genSize; j++)
					distance[i][j] = sc.nextInt();
			sc.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	//create all populations
	private void generatePopulations()
	{
		populations = new NumberPopulation[popCount];
		
		populations[0] = new NumberPopulation(popSize, genSize, flow, distance, ratio);
		int currPop = 1;
		while(currPop<popCount)
		{
			populations[currPop] = new NumberPopulation(populations[currPop-1], crossProbability, mutateProbability);
			currPop++;
		}
	}
	
	//calculate quality ratio for algorythm
	private void calcRatio ()
	{
		int maxFlow=0;
		int maxDist=0;
		for (int i=0; i<genSize; i++)
		{
			for (int j=0; j<genSize; j++)
			{
				if (maxFlow<flow[i][j])
					maxFlow = flow[i][j];
				if (maxDist<distance[i][j])
					maxDist = distance[i][j];
			}
		}
		ratio = maxFlow*maxDist*genSize*genSize*100;
	}
	
	
	//print results of algorythm
	public void getResults()
	{
		for (int i=0; i<popCount; i++)
		{
			System.out.println("Populacja nr:" + (i+1));
			System.out.println(populations[i].getBest().getQuality() + " " + populations[i].getAverage() + " " + populations[i].getWorst().getQuality());
			System.out.println(populations[i].getBest().getGens());
			System.out.println();
		}
	}
}
