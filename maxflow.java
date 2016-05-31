import java.util.*;
import java.util.Scanner;
import java.util.Queue;  
import java.util.Iterator;

public class maxflow
{
    
	private int nodeNum;
	private boolean[] visit;
	private int[] head;
	private Queue<Integer> queue;
	private ArrayList<Integer> sourceVertex;
	private ArrayList<Integer> sinkVertex;

	//init
	public maxflow (int numNode)
	{		
		this.nodeNum = numNode;
		this.visit = new boolean[numNode];
		this.head = new int[numNode];
		this.queue = new LinkedList<Integer>();
		sourceVertex = new ArrayList<Integer>();
        sinkVertex = new ArrayList<Integer>();
	}


	//search
	public boolean bfS(int start, int goal, int[][] residual)
	{		
		int destination, now;
		boolean foundPath = false;
		//init
		for(int vertex = 0; vertex < nodeNum; vertex++){
			visit[vertex] = false;
			head[vertex] = -1;
		}
		queue.offer(start);
		visit[start] = true;
		while(!queue.isEmpty())
		{
			now = queue.poll();
			destination = 0;

			while(destination < nodeNum)
			{
				if(residual[now][destination] > 0 && !visit[destination])
				{
					head[destination] = now;
					queue.offer(destination);
					visit[destination] = true;
				}
				destination++;
			}
		}
		if(visit[goal])
			foundPath = true;
		return foundPath;

	}



	//flow- main function
 	public int maxflow(int graph[][], int source, int sink)
 	{	
 		int[][] residual = new int[nodeNum][nodeNum];
 		int path;
 		int maxF = 0;
 		int vertex;

 		for(int a = 0; a < nodeNum; a++)
		{
			for(int b = 0; b < nodeNum; b++)
				 residual[a][b] = graph[a][b];
		}

		while (bfS(source, sink, residual))
		{	
			path = Integer.MAX_VALUE;
			for(vertex = sink;vertex!= source;vertex = head[vertex] )
			{	
				path = Math.min(path,residual[head[vertex]][vertex]);
			}
			for(vertex = sink; vertex!= source;vertex = head[vertex] )
			{
				residual[head[vertex]][vertex] -= path;
	/**/		residual[vertex][head[vertex]] += path;			
			}		
			maxF += path;	



			/******* debug *****  debug  *****/
/*
			for(int a = 0; a < nodeNum; a++)
			{
			for(int b = 0; b < nodeNum; b++)
				 System.out.print(residual[a][b]);
				System.out.println("");
			}
			System.out.println("");	
			try{
			Thread.sleep(10000);
			}catch(InterruptedException e){
				System.out.println("Error");
			}
			/********* debug debug debug *************/
		}


		//turn to cut	
		for(vertex = 0; vertex < nodeNum; vertex++)
		{
			if(bfS(source, vertex, residual))
				sourceVertex.add(vertex);
			else
				sinkVertex.add(vertex);
		}

		for (int a = 0; a < sourceVertex.size(); a++) 
		{
			for(int b = 0; b < sinkVertex.size(); b++)
			{
				if(graph[sourceVertex.get(a)][sinkVertex.get(b)] > 0)
					System.out.println(sourceVertex.get(a) + "-" + sinkVertex.get(b));
				else if(graph[sinkVertex.get(b)][sourceVertex.get(a)] > 0)
					System.out.println(sinkVertex.get(b) + "-" + sourceVertex.get(a));
						
				
			}
		}


		return maxF;
 	}

 	

//	public static void main(String[] args)
//	{
//		int nodeNum;
//		int source;
//		int sink;
//		int flow;
//
//		int[][] graph;
//		System.out.println("Plz input node number: ");
//		Scanner input = new Scanner(System.in);
//		nodeNum = input.nextInt();
//		graph = new int[nodeNum][nodeNum];
//
//		System.out.println("Plz input graph: ");
//		// a->b
//		for(int a = 0; a < nodeNum; a++)
//		{
//			for(int b = 0; b < nodeNum; b++)
//				graph[a][b] = input.nextInt();
//		}
//		System.out.print("Which node is source ( ");
//		for(int a = 0; a < nodeNum; a++)
//			System.out.print("node" + a + " ");
//		System.out.println("): ");
//		source = input.nextInt();
//		System.out.print("Which node is sink ( ");
//		for(int a = 0; a < nodeNum; a++)
//			System.out.print("node" + a + " ");
//		System.out.println("): ");
//		sink = input.nextInt();
//		maxflow maxflowA = new maxflow(nodeNum);
//		flow = maxflowA.maxflow(graph,source,sink);
//		System.out.println("Max flow = " + flow);
//	}

	
}