import java.util.LinkedList;
import java.util.Random;

public class NumberGenotype
{
	private int[] gens;
	private int quality;
	private final int[][] flow;
	private final int[][] distance;
	private int size;
	
	//contructor for random genotype
	public NumberGenotype(int size, int[][] fl, int [][] dist)
	{
		this.size = size;
		gens = new int[size];
		genRandomGens();
		flow = fl;
		distance = dist;
	}
	
	//constructor for crossed genotype
	public NumberGenotype(NumberGenotype r1, NumberGenotype r2)
	{
		size = r1.getSize();
		flow = r1.flow;
		distance = r1.distance;
		gens = new int[size];
		for (int i=0; i< size/2; i++)
		{
			gens[i] = r1.getGen(i);
		}
		for (int i=size/2; i<size; i++)
		{
			gens[i] = r2.getGen(i);
		}
		repair();
	}
	
	//constructor for copied genotype
	public NumberGenotype (NumberGenotype toCopy, int[][] fl, int[][] dist)
	{
		flow = fl;
		distance = dist;
		size = toCopy.getSize();
		gens = new int[size];
		for (int i=0; i<size; i++)
			gens[i]=toCopy.getGen(i);
	}
	
	//generate genotype with random gens
	private void genRandomGens()
	{
		Random rand = new Random();
		for (int i=0; i<size; i++)
		{
			gens[i]= rand.nextInt(size);
		}
		repair();
	}
	
	//mutate genotype
	public void mutate ()
	{
		int temp = gens[0];
		gens[0]=gens[size-1];
		gens[size-1]=temp;
	}
	
	//repair genotype
	private void repair()
	{
		LinkedList<Integer> notUsedGens = new LinkedList<Integer>();
		LinkedList<Integer> replaceThoseGens = new LinkedList<Integer>();
		Random rand = new Random();
		for (int i=0; i<size; i++)
		{
			notUsedGens.add(i);
		}
		for (int i=0; i<size; i++)
		{
			if (notUsedGens.contains(gens[i]))
				notUsedGens.remove(new Integer(gens[i]));
			else
				replaceThoseGens.add(i);
		}
		for (int tempS = replaceThoseGens.size(); tempS>0; --tempS)
		{
			gens[replaceThoseGens.removeFirst()] = notUsedGens.remove(rand.nextInt(tempS));
		}
	}

	//calculate quality of genotype
	public void calculateQuality(int ratio)
	{
		int temp = 0;
		for (int i=0; i<size; i++)
		{
			for (int j=0; j<size; j++)
			{
				temp += flow[gens[i]][gens[j]] * distance[i][j];
			}
		}
		quality = ratio/temp;
	}
	
	//getters for population
	public int getQuality()
	{
		return quality;
	}
	
	//getters for genotype
	public int getSize()
	{
		return size;
	}
	
	public int getGen(int pos)
	{
		if (pos<size)
			return gens[pos];
		else
			return -1;
	}
	
	//getters for algorythm
	public String getGens()
	{
		String display = " ";
		for (int i=0; i<size; i++)
		{
			display+= (gens[i]+1) + ",";
		}
		return display;
	}
}
