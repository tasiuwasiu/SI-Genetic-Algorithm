import java.util.Random;

public class NumberPopulation
{

	private NumberGenotype[] genotypes;
	private int[] genotypesQuality;
	private int popSize;
	private final int[][] flow;
	private final int[][] distance;
	private int crossProb;
	private int mutProb;
	private int ratio;
	
	//constructor for first population
	public NumberPopulation (int size, int genSize, int[][] flow, int[][] dist, int ratio)
	{
		popSize = size;
		genotypes = new NumberGenotype[size];
		this.flow = flow;
		distance = dist;
		this.ratio=ratio;
		for (int i=0; i<size; i++)
			genotypes[i] = new NumberGenotype(genSize, flow, distance);
		calculateQualities();
	}
	
	//constructor for next population
	public NumberPopulation (NumberPopulation previous, int crossProb, int mutProb)
	{
		flow = previous.getFlow();
		distance = previous.getDistance();
		this.crossProb = crossProb;
		this.mutProb = mutProb;
		ratio=previous.getRatio();
		popSize = previous.getSize();
		genotypes = new NumberGenotype[popSize];
		setNewPopulation(previous);
		calculateQualities();
	}
	
	//create new population with crossing and mutation
	private void setNewPopulation(NumberPopulation previous)
	{
		for (int i=0; i<popSize; i++)
		{
			boolean willMutate = getRandomBool(mutProb);
			boolean willCross = getRandomBool(crossProb);
			NumberGenotype temp = new NumberGenotype(getRandomGenotype(previous), flow, distance);
			if (willCross)
			{
				temp = new NumberGenotype( temp, getRandomGenotype(previous));
			}
			if (willMutate)
				temp.mutate();
			genotypes[i] = temp;
		}
	}
	
	//return random genotype with calculated probability
	private NumberGenotype getRandomGenotype(NumberPopulation previous)
	{
		long[] weights = new long[popSize];
		int[] qualities = previous.getQualities();
		for (int i=0; i<popSize; i++)
		{
			weights[i] = qualities[i]-(int)(previous.getAverage()*0.9);
		}
		
		int currItem = 0;
		int[] info = new int[ratio];
		
		for (int i=0; i<popSize; i++)
		{
			while (weights[i]>0)
			{
				info[currItem] = i;
				weights[i]--; 
				currItem++;
			}
		}
		
		Random rand = new Random();
		return previous.getSpecific(info[rand.nextInt(currItem)]);
	}
	
	//return random bool with probability
	private boolean getRandomBool(int prob)
	{
		Random rand = new Random();
		return rand.nextInt(100) < prob;
	}
	
	//calculate quality for all genotypes
	private void calculateQualities()
	{
		genotypesQuality = new int[popSize];
		for (int i=0; i<popSize; i++)
		{
			genotypes[i].calculateQuality(ratio);
			genotypesQuality[i] = (genotypes[i].getQuality());
		}
	}
	
	//getters for population
	public int[][] getFlow()
	{
		return flow;
	}
	
	public int[][] getDistance()
	{
		return distance;
	}
	
	public int getSize()
	{
		return popSize;
	}
	
	public int getRatio()
	{
		return ratio;
	}
	
	public NumberGenotype getSpecific(int i)
	{
		return genotypes[i];
	}
	
	public int[] getQualities()
	{
		return genotypesQuality;
	}
	
	//getters for algorythm
	public NumberGenotype getBest()
	{
		int max=Integer.MIN_VALUE;
		int maxId = -1;
		
		for (int i=0; i<popSize; i++)
		{
			if (genotypesQuality[i]>max)
			{
				max=genotypesQuality[i];
				maxId = i;
			}
		}
		return genotypes[maxId];
	}
	
	public NumberGenotype getWorst()
	{
		int min=Integer.MAX_VALUE;
		int minId = -1;
		
		for (int i=0; i<popSize; i++)
		{
			if (genotypesQuality[i]<min)
			{
				min=genotypesQuality[i];
				minId = i;
			}
		}
		return genotypes[minId];
	}
	
	public int getAverage()
	{
		int sum = 0;
		for (int qt : genotypesQuality)
		{
			sum+=qt;
		}
		return sum/genotypesQuality.length;
	}
	
}
