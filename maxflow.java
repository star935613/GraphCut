import java.util.*;

public class maxflow
{
	private int nodeNum;
	private boolean[] visit;
	private int[] head;
	private Queue<Integer> queue;
	private ArrayList<Integer> sourceVertex;
	private ArrayList<Integer> sinkVertex;
	private int afterSource;
	private int preSink;
	private int wide;
	private int height;

	//init
	public maxflow (int nodeNum, int wide, int height)
	{		
		this.nodeNum = nodeNum;
		this.visit = new boolean[nodeNum];
		this.head = new int[nodeNum];
		this.wide = wide;
		this.height = height;
		this.queue = new LinkedList<Integer>();
		sourceVertex = new ArrayList<Integer>();
        sinkVertex = new ArrayList<Integer>();
	}

	public void init()
	{
		for(int vertex = 0; vertex < nodeNum; vertex++){
			visit[vertex] = false;
			head[vertex] = -1;
		}

		while(!queue.isEmpty()){
			queue.poll();
		}
		afterSource = -1;
		preSink = -1;
	}
	//up down left right
	public int destinationN(int now,int near){
		int next;
		switch(near){
			case 1:
				next = now - wide;
				return next;
			case 2:
				next = now + wide;
				return next;
			case 3:
				next = now - 1;
				return next;
			case 4:
				next = now + 1;
				return next;
			case 5:
				next = nodeNum;
				return next;	
		}
		return -1;
	}

	public int relation(int node, int nextNode){	
	    int addr = node - nextNode; 		
		//int leftUp = wide + 1;
		int up = wide;
		//int rightUp = wide - 1;
		int left = 1;
		int right = -1;
		//int leftDown = -1*wide + 1;
		int down = -1*wide;
		//int rightDown = -1*wide - 1;
		//if(addr == leftUp) 
		//	return 5;				
		if(addr == up) 
			return 1;
		//else if(addr == rightUp)
		//	return 6;
		else if(addr == left)
			return 3;
		else if(addr == right)
			return 4;
		//else if(addr == leftDown)
		//	return 7;
		else if(addr == down)
			return 2;
		//else if(addr == rightDown)
		//	return 8;
		else
			return 5;
	}
	//bfs -- s->t
	public boolean bfS(List<List<List<Double>>> residual)   
	{
		int destination, now, start;
		int nextNode;
		boolean foundPath = false;		
		init();

		for(int x = 0; x < nodeNum; x++){
			if(residual.get((int)x/wide).get((int)x%wide).get(0) <= 0)
				continue; 
		//	System.out.println(foundPath);
			start = x;
			queue.offer(start);			
			visit[start] = true;
			
			while(!queue.isEmpty())
			{
			//	System.out.println("1");
				now = queue.poll();
				destination = 1;

				while(destination < 6)
				{
				//	System.out.println("2");
					nextNode = destinationN(now, destination);
				//	System.out.println(now + " " + nextNode + " " + destination + " " + residual.get((int)now/wide).get((int)now%wide).get(destination));
					if(nextNode == nodeNum && residual.get((int)now/wide).get((int)now%wide).get(5)>0){
				//		System.out.println("4");
						foundPath = true;
						preSink = now;
//						System.out.println("preSink="+now);
						afterSource = start;
						return foundPath;				
					}
					else if(residual.get((int)now/wide).get((int)now%wide).get(destination) > 0 && !visit[nextNode])
					{
				//		System.out.println("3");
						head[nextNode] = now;
						queue.offer(nextNode);
						visit[nextNode] = true;
					}
					destination++;
				}

			}
		}

		return foundPath;

	}
	
	public boolean BFS(List<List<List<Double>>> residual, int end)   
	{
		int destination, now, start;
		int nextNode;
		boolean foundPath = false;		
		init();

		for(int x = 0; x < nodeNum; x++){
			if(residual.get((int)x/wide).get((int)x%wide).get(0) <= 0)
				continue; 
		//	System.out.println(foundPath);
			start = x;
			if(start == end)
				return true;
			queue.offer(start);			
			visit[start] = true;
			
			while(!queue.isEmpty())
			{
			//	System.out.println("1");
				now = queue.poll();
				destination = 1;

				while(destination < 6)
				{
				//	System.out.println("2");
					nextNode = destinationN(now, destination);
					if(nextNode == nodeNum && residual.get((int)now/wide).get((int)now%wide).get(5)>0){			
						return foundPath;				
					}
					else if(residual.get((int)now/wide).get((int)now%wide).get(destination) > 0 && !visit[nextNode])
					{
						if(nextNode == end)
							return true;
						head[nextNode] = now;
						queue.offer(nextNode);
						visit[nextNode] = true;
					}
					destination++;
				}

			}
		}

		return foundPath;

	}
/*
	public boolean bfs(int[][] residual, int dest){	
		int destination, now, start;
		init();
		int nextNode;
		for(int x = 0; x < nodeNum; x++){
			if(residual[x][0] <= 0)
				continue; 
			start = x;
			queue.offer(start);			
			visit[start] = true;			
			while(!queue.isEmpty())
			{
				now = queue.poll();
				destination = 1;
				while(destination < 6)
				{
					nextNode = destinationN(now, destination);
					if(residual[now][destination] > 0 && !visit[nextNode])
					{						
					//	head[nextNode] = now;						
						if(nextNode == dest)
							return true;
						queue.offer(nextNode);
						visit[nextNode] = true;
					}
					destination++;
				}
			}			
		}
		return false;

	}
	*/
	public int maxflow(List<List<List<Double>>> graph)
 	{	
 		List<List<List<Double>>> residual;
 		double path;
 		int maxF = 0;
 		int vertex;

// 		for(int a = 0; a < nodeNum; a++)
//		{
//			for(int b = 0; b < 6; b++)
				 residual = graph;
//		}

/**/	while (bfS(residual))
		{	
		//	System.out.println("123");
			path = Integer.MAX_VALUE;
			path = Math.min(path,residual.get((int)preSink/wide).get((int)preSink%wide).get(5));
//			System.out.println(preSink+"->T=" + residual[preSink][9]);
			for(vertex = preSink;vertex!= afterSource;vertex = head[vertex] )
			{	
				path = Math.min(path, residual.get((int)head[vertex]/wide).get((int)head[vertex]%wide).get(relation(head[vertex], vertex)));
//				System.out.println(head[vertex] + "->" + vertex + " = " +residual[head[vertex]][relation(head[vertex], vertex)]+" "+head[vertex]+" ,"+relation(head[vertex], vertex));
			}
			path = Math.min(path, residual.get((int)afterSource/wide).get((int)afterSource%wide).get(0));
//			System.out.println("S->afterSource=" + residual[afterSource][0]);
			residual.get((int)preSink/wide).get((int)preSink%wide).set(5, residual.get((int)preSink/wide).get((int)preSink%wide).get(5) - path);
//			residual.get(1).get(1).get(1) -= path;
			for(vertex = preSink; vertex!= afterSource;vertex = head[vertex])
			{
				residual.get((int)head[vertex]/wide).get((int)head[vertex]%wide).set(relation(head[vertex], vertex), residual.get((int)head[vertex]/wide).get((int)head[vertex]%wide).get(relation(head[vertex], vertex)) - path);
				residual.get((int)vertex/wide).get((int)vertex%wide).set(relation(vertex, head[vertex]), residual.get((int)vertex/wide).get((int)vertex%wide).get(relation(vertex, head[vertex])) + path);			
			}
			residual.get((int)afterSource/wide).get((int)afterSource%wide).set(0, residual.get((int)afterSource/wide).get((int)afterSource%wide).get(0) - path);	
			maxF += path;	
		}
		
		//turn to cut	
		for(vertex = 0; vertex < nodeNum; vertex++)
		{		
		
			if(BFS(residual, vertex)){
				sourceVertex.add(vertex);
				System.out.printf("S");
			}
			else{
				sinkVertex.add(vertex);
				System.out.printf("T");
			}
			if ((vertex+1) % wide == 0 ){
				System.out.println("");
			}
		
		}
	/*	
		for (int a = 0; a < sourceVertex.size(); a++) 
		{
			for(int b = 0; b < sinkVertex.size(); b++)
			{
				if(graph.get((int)sourceVertex.get(a)/wide).get((int)sourceVertex.get(a)%wide).get(relation(sourceVertex.get(a), sinkVertex.get(b))) > 0)
					System.out.println(sourceVertex.get(a) + "-" + sinkVertex.get(b));
				else if(graph.get((int)sinkVertex.get(b)/wide).get((int)sinkVertex.get(b)%wide).get(relation(sinkVertex.get(b), sourceVertex.get(a))) > 0)
					System.out.println(sinkVertex.get(b) + "-" + sourceVertex.get(a));
						
				
			}
		}

  */
		return maxF;
 	}

//	public static void main(String[] args)
//	{
//		int nodeNum;
//		int flow;
//		int height;
//		int wide;
//		int[][] graph;
//		System.out.println("Plz input node number: ");
//		Scanner input = new Scanner(System.in);
//		nodeNum = input.nextInt();
//		graph = new int[nodeNum][6];
//		System.out.println("Plz input graph height: ");
//		height = input.nextInt();		
//		System.out.println("Plz input graph wide: ");
//		wide = input.nextInt();
//		System.out.println("Plz input graph: ");
//		// a->b
//		for(int a = 0; a < nodeNum; a++)
//		{
//			for(int b = 0; b < 6; b++)
//				graph[a][b] = input.nextInt();
//		}
//
//		maxflow maxflowA = new maxflow(nodeNum, wide, height);
//		flow = maxflowA.maxflow(graph);
//		System.out.println("Max flow = " + flow);
//	}

}