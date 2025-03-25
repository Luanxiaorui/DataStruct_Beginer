package Dao;

import Subject.Vertice;
import Subject.Edge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AdjacentList {

    final int Max = 0x7f7f7f7f;
    public String[] city;
    public String[] planeCity;
    Vertice[] train;//实现图的储存
    Vertice[] vertices;
    Vertice[] plane;
    public int[] v_num;//0代表train，1代表plane
    int[] e_num;

    public AdjacentList() {

        train=new  Vertice[100];
        planeCity = new String[100];
        plane = new Vertice[100];
        city = new String[100];
        vertices = new Vertice[100];
        e_num=new int[2];
        v_num = new int[2];

    }

    //运行时从文件读入信息，建立图
    public void create() {
        try {
            String str;
            BufferedReader in = new BufferedReader(new FileReader("D:\\IdeaProjects\\National Traffic Consultation Simulation System\\cityTrain.txt"));
            while ((str = in.readLine()) != null) {
                String[] information = str.split(" ");
                city[Integer.parseInt(information[0])] = information[1];
                addVertical(information[1], 0);
            }
            BufferedReader in2 = new BufferedReader(new FileReader("D:\\IdeaProjects\\National Traffic Consultation Simulation System\\cityPlane.txt"));
            while ((str = in2.readLine()) != null) {
                String[] information = str.split(" ");
                planeCity[Integer.parseInt(information[0])] = information[1];
                addVertical(information[1], 1);
            }
        } catch (IOException e) {
            System.out.println("wrong");
        }
        try {
            String str;
            BufferedReader in = new BufferedReader(new FileReader("D:\\IdeaProjects\\National Traffic Consultation Simulation System\\plane.txt"));
            while ((str = in.readLine()) != null) {

                String[] information = str.split(" ");
                String start = planeCity[Integer.parseInt(information[0])], end = planeCity[Integer.parseInt(information[1])];
                int time = Integer.parseInt(information[2]) / 800*60;
                int cost = Integer.parseInt(information[3]);
                for (int i = 5; i < information.length - 1; i += 2) {
                    String startTime = information[i];
                    String number = "NG" + information[i + 1];
                    addEdge(start, end, cost, number, startTime, time, 1);
                }
            }
            BufferedReader in2 = new BufferedReader(new FileReader("D:\\IdeaProjects\\National Traffic Consultation Simulation System\\train.txt"));
            while ((str = in2.readLine()) != null) {

                String[] information = str.split(" ");
                String start = city[Integer.parseInt(information[0])], end = city[Integer.parseInt(information[1])];
                int time = Integer.parseInt(information[2]) / 400*60;
                int cost = Integer.parseInt(information[3]);
                for (int i = 5; i < information.length - 1; i += 2) {
                    String startTime = information[i];
                    String number = information[i + 1];
                    addEdge(start, end, cost, number, startTime, time, 0);
                }
            }
        } catch (IOException e) {
            System.out.println("wrong");
        }
    }

    public void update(int index) {
        if (index == 0) train = vertices;
        else plane = vertices;
        vertices = new Vertice[100];
    }

    public void downDate(int index) {
        if (index == 0) vertices = train;
        else vertices = plane;
    }
    //得到节点下标
    public int getIndex(String v, int index) {

        downDate(index);
        for (int i = 0; i < v_num[index]; i++)
            if (vertices[i] != null && !vertices[i].start.isEmpty() && vertices[i].start.equals(v))
                return i;
        return -1;

    }

    public Edge getMinVCost(String v1, String v2, String time, int index) {

        downDate(index);
        int i = getIndex(v1, index);
        Edge e = vertices[i].next;
        while (e != null) {
            if (!e.arrive.isEmpty() && !e.start_time.isEmpty() && e.arrive.equals(v2) && compar(time, e.start_time)) {
                return e;
            }
            e = e.next;
        }
        return null;
    }
    private boolean compar(String time, String startTime) {
        //比较时间前后,time在startTime之前返回true，否则false;
        if (time.isEmpty()) return true;
        return !(Integer.parseInt(time) > Integer.parseInt(startTime));
    }
    public Edge getMinVTime(String v1, String v2, String time, int index) {

        downDate(index);
        int i = getIndex(v1, index);
        int min_time = Max;
        Edge e = vertices[i].next;
        Edge tmp=null;
        while (e != null) {
            if (e.arrive.equals(v2) && compar(time, e.start_time)) {
           //     min_time = Math.min(min_time, getStayTime(time, e.start_time) + e.time);
                if (min_time>getStayTime(time,e.arrive_Time))
                {
                    min_time=getStayTime(time,e.arrive_Time);
                    tmp=e;
                }
            }
            e = e.next;
        }
        return tmp;

    }
    private int getStayTime(String start, String end) {
        if(start.isEmpty()||end.isEmpty()) return 0;
        int time1,time2;
        //end-time 的时间（以分钟为单位）
        if(end.length()==3) {
             time1=(end.charAt(0)-'0')*60+Integer.parseInt(end.substring(1,3));
        }
        else{
            time1=Integer.parseInt(end.substring(0,2))*60+Integer.parseInt(end.substring(2,4));
        }
        if(start.length()==3) {
            time2=(start.charAt(0)-'0')*60+Integer.parseInt(start.substring(1,3));
        }
        else{
            time2=Integer.parseInt(start.substring(0,2))*60+Integer.parseInt(start.substring(2,4));
        }
          System.out.println(time1+" "+time2);
        return time1-time2;
    }

    //实现节点的删除
    public boolean deleteVertical(String vertical, int index) {

        int i = getIndex(vertical, index);
        if (i == -1) return false;

        downDate(index);

        for (; i < v_num[index]; i++)
            vertices[i] = vertices[i + 1];//删除出度节点
        v_num[index]--;

        //删除入度边
        for (i = 0; i < v_num[index]; i++) {
            Edge e=null;
            if(vertices[i].next!=null) e= vertices[i].next;
            else  continue;
            while (e.arrive.equals(vertical)) {
                vertices[i].next = vertices[i].next.next;
                e_num[index]--;
                e = vertices[i].next;
            }
            while (e.next != null) {
                if (e.next.arrive.equals(vertical)) {
                    e.next = e.next.next;
                    e_num[index]--;
                    continue;
                }
                e = e.next;
            }
            vertices[i].end = e;
        }
        update(index);
        return true;
    }
    // 实现节点添加
    public void addVertical(String vertical, int index) {

        if (getIndex(vertical, index) != -1) return;
        downDate(index);
        vertices[v_num[index]] = new Vertice(vertical);
        v_num[index]++;
        update(index);

    }

    //实现边的删除
    public boolean deleteSpecialEdge(String start, String end, String number, int index) {

        int i = getIndex(start, index), j = getIndex(end, index);
        if (i == -1 || j == -1) return false;
        downDate(index);
        Edge e = vertices[i].next;
        while (e.arrive.equals(end) && e.number.equals(number)) {
            vertices[i].next = vertices[i].next.next;
            e_num[index]--;
            e = vertices[i].next;
        }
        while (e.next != null) {
            if (e.next.arrive.equals(end) && e.number.equals(number)) {
                e.next = e.next.next;
                e_num[index]--;
                continue;
            }
            e = e.next;
        }
        vertices[i].end = e;

        update(index);
        return true;

    }
    public boolean delete(String start, String end, String number, int index) {
        //删除一个顶点，或者一条边
        
        boolean f1=start.isEmpty(),f2=end.isEmpty(),f3=number.isEmpty();
        if(!f3&&!f1&&!f2)      return deleteSpecialEdge(start,end,number,index);         
        else if (f3&&!f1&&!f2) return deleteEdge(start,end,index);
        else if (f1&&!f2) return deleteVertical(end, index);
        else  if(!f1) return deleteVertical(start,index);
        else if(!f3)  return deleteNumber(number,index);

       return false;
    }
    //实现边的添加
    public void addEdge(String start, String end, int cost, String number, String start_time, int time, int index) {

        downDate(index);
        int i;
        if (getIndex(start, index) == -1) addVertical(start, index);
        if (getIndex(end, index) == -1) addVertical(end, index);
        i = getIndex(start, index);
        Edge e = new Edge(cost, end, number, start_time, time, start);
        if (vertices[i].end == null) {
            vertices[i].next = vertices[i].end = e;
        } else {
            vertices[i].end.next = e;
            vertices[i].end = e;
        }
        update(index);
        e_num[index]++;
    }
    public String[][] DijkStra_cost(String start, String end,int indicate) {

        int pre = getIndex(start, indicate), en = getIndex(end, indicate);
        if (pre == -1 || en == -1) return null;//不存在，何谈最短路

        downDate(indicate);

        int minCost, index ;//记录每次最少花费,以及下标
        int[] cost = new int[v_num[indicate]];//存储最少花费数目
        String time ;//记录每次到站时间，继续寻找
        Edge tmp, min ;//分别用来临时记录和取最小
        Edge[] e = new Edge[v_num[indicate] + 1];//存放路径
        boolean[] visited = new boolean[v_num[indicate] + 1];//记录是否为最短
        boolean flag = true;//最短路径寻找完毕

        //初始化数据
        for (int i = 0; i < v_num[indicate]; i++)
            if (pre != i) cost[i] = Max;
        time = vertices[pre].next.start_time;
        visited[pre] = true;
        //dijkstra
        while (flag) {

            flag = false;
            minCost = Max;
            min=null;
            for (int i = 0; i < v_num[indicate]; i++) {
                if (!visited[i]) {
                    tmp = getMinVCost(vertices[pre].start, vertices[i].start, time, indicate);
                    if (tmp!=null&&cost[i] > cost[pre] + tmp.cost) {
                        cost[i] = Math.min(cost[pre] + tmp.cost, cost[i]);
                        cost[i] = cost[pre] + tmp.cost;
                        e[i] = tmp;
                    }
                    flag = true;//继续循环
                }
            }
            for (int i = 0; i < v_num[indicate]; i++) {
                if (e[i] != null && !visited[i] && cost[i] < minCost) {
                    minCost = cost[i];
                    min = e[i];
                }
            }
            if (min != null) index = getIndex(min.arrive, indicate);
            else break;
            visited[index] = true;
            time = min.arrive_Time;
            pre = index;
        }
        //返回值以对应形式        
        return getCovert(e,indicate,en);
    }
    private String convertTo(String s) {
        String str = "";
        int k = s.length();
        if (k == 3) {
            str += s.charAt(0);
            str += ":";
            str += s.charAt(1);
            str += s.charAt(2);
        }
        if (k == 4) {
            str += s.charAt(0);
            str += s.charAt(1);
            str += ":";
            str += s.charAt(2);
            str += s.charAt(3);
        }
        return str;
    }
    //根据最优原则，获取最优路线
    public Object[][] getBestWay(String start,String end,String way,String condition) {
        Object[][] t;
        if(condition.equals("最少花费")&&way.equals("火车"))
            t = DijkStra_cost(start,end,0);
        else if (condition.equals("最少花费")&&way.equals("飞机")) {
            t=DijkStra_cost(start,end,1);
        }
        else if (condition.equals("最快路径")&&way.equals("飞机")) {
            t=DijkStra_Time(start,end,1);
        }
        else if (condition.equals("最快路径")&&way.equals("火车")) {
            t=DijkStra_Time(start,end,0);
        } else if (condition.equals("最少中转")&&way.equals("火车")) {
            t=minTurnNum(start, end,0);
        }
        else {
            t=minTurnNum(start, end,1);
        }
        return t;
    }
    //遍历邻接表，达成
    public Object[][] getNewInformation(int index) {
        Object[][] t = new Object[e_num[index]+3][6];
        int p=0;
        downDate(index);
        for(int i=0;i<v_num[index];i++){
            Edge tmp=null;
            if(vertices[i]!=null) tmp=vertices[i].next;
            while (tmp != null) {
                t[p][0]=tmp.start;
                t[p][1]=tmp.arrive;
                t[p][2]=convertTo(tmp.start_time);
                t[p][3]=convertTo(tmp.arrive_Time);
                t[p][4]=tmp.number;
                t[p][5]=tmp.cost;
                p++;
                tmp=tmp.next;
            }
        }
        return t;
    }

    public Object[][] getSingleStation(String station,int index,boolean flag){
        Object[][] t=new Object[e_num[index]][6];
        int i=getIndex(station,index),p=0;
        downDate(index);
        if(flag) {
            Edge tmp = null;
            if (vertices[i] != null) tmp = vertices[i].next;
            while (tmp != null) {
                t[p][0] = tmp.start;
                t[p][1] = tmp.arrive;
                t[p][2] = convertTo(tmp.start_time);
                t[p][3] = convertTo(tmp.arrive_Time);
                t[p][4] = tmp.number;
                t[p][5] = tmp.cost;
                p++;
                tmp = tmp.next;
            }
        }else {
            for (int j = 0; j < v_num[index]; j++) {
                Edge tmp = null;
                if(vertices[j].next!=null) tmp=vertices[j].next;
                while (tmp != null) {
                    if (tmp.arrive.equals(station)) {
                        t[p][0] = tmp.start;
                        t[p][1] = tmp.arrive;
                        t[p][2] = convertTo(tmp.start_time);
                        t[p][3] = convertTo(tmp.arrive_Time);
                        t[p][4] = tmp.number;
                        t[p][5] = tmp.cost;
                        p++;
                    }
                    tmp = tmp.next;
                }
            }
        }
        return t;
    }

    public Object[][] getDoubleStation(String start,String end,int index){
        Object[][] t=new Object[30][6];
        int i=getIndex(start,index),p=0;
        Edge tmp=null;
        downDate(index);
        if(vertices[i]!=null) tmp=vertices[i].next;
        while(tmp!=null){
            if(tmp.arrive.equals(end)) {
                t[p][0] = tmp.start;
                t[p][1] = tmp.arrive;
                t[p][2] = convertTo(tmp.start_time);
                t[p][3] = convertTo(tmp.arrive_Time);
                t[p][4] = tmp.number;
                t[p][5] = tmp.cost;
                p++;
            }
            tmp=tmp.next;
        }
        return t;
    }

    public Object[][] getInformation(String number,String start,String end,int index) {
        if((getIndex(start,index)==-1&&!start.isEmpty()) || (getIndex(end,index)==-1&&!end.isEmpty()) ) return null;
        downDate(index);
        if(!number.isEmpty()) {
            Object[][] t = new Object[e_num[index]][6];
            int p = 0;
            for (int i = 0; i < v_num[index]; i++) {
                Edge tmp = null;
                if(vertices[i].next!=null) tmp=vertices[i].next;
                while (tmp != null) {
                    if (tmp.number.equals(number)) {
                        t[p][0] = tmp.start;
                        t[p][1] = tmp.arrive;
                        t[p][2] = convertTo(tmp.start_time);
                        t[p][3] = convertTo(tmp.arrive_Time);
                        t[p][4] = tmp.number;
                        t[p][5] = tmp.cost;
                        p++;
                        break;
                    }
                    tmp = tmp.next;
                }
            }
            return t;
        } else if (!start.isEmpty()&&!end.isEmpty()) {
            return getDoubleStation(start,end,index);
        } else if (!start.isEmpty()) {
            return getSingleStation(start,index,true);
        }
        else return getSingleStation(end,index,false);
    }

    public void modify(String start, String end, String number, String startTime,int cost, int time,int index) {
        int i;
        if ((i = getIndex(start,index)) == -1) return;
        downDate(index);
        Edge e = vertices[i].next;
        while (e != null) {
            if (e.number.equals(number) && e.arrive.equals(end)) {
                e.start_time=startTime;
                e.cost = cost;
                e.time = time;
                e.arrive_Time=e.getTime(time,startTime);
                break;
            }
            e=e.next;
        }
        update(index);
    }

    public void updateInformation(String start, String end, String number, String cost,String startTime, String arriveTime,int index) {
        modify(start, end, number,CoverToNUmber(startTime),Integer.parseInt(cost), getStayTime(CoverToNUmber(startTime),CoverToNUmber(arriveTime)),index);
    }
    //时间转数字
    public String CoverToNUmber(String time){
        String s="";
        for(int i=0;i<time.length();i++)
        {
            if(time.charAt(i)<='9'&&time.charAt(i)>='0')
                s+=time.charAt(i);
        }
        return s;
    }
    //删除指定车次
    private boolean deleteNumber(String number,int index) {
        downDate(index);
        for (int i = 0; i < v_num[index]; i++) {
            Edge tmp=null ;
            if(vertices[i]!=null&&vertices[i].next!=null) {
                tmp=vertices[i].next;
                if(tmp.number.equals(number)) {
                    vertices[i].next = tmp.next;
                    continue;
                }
            }
            if (tmp != null) {
                while (tmp.next != null) {
                    if (tmp.next.number.equals(number)) {
                          tmp.next=tmp.next.next;
                          break;
                    }
                    tmp = tmp.next;
                }
            }
        }
        update(index);
        return true;
    }
    //删除两站之间所有边
    private boolean deleteEdge(String start, String end,int index) {
        downDate(index);
        for (int i = 0; i < v_num[index]; i++) {
            Edge tmp=null ;
           if(vertices[i]!=null&&vertices[i].next!=null) {
                tmp=vertices[i].next;
                while(tmp.start.equals(start)&&tmp.arrive.equals(end)) {
                    vertices[i].next = tmp.next;
                    tmp=tmp.next;
                }

            }
            if (tmp != null) {
                while (tmp.next != null) {
                    if (tmp.start.equals(start)&&tmp.arrive.equals(end)) {
                        tmp.next=tmp.next.next;
                        break;
                    }
                    tmp = tmp.next;
                }
            }
        }
        update(index);
        return true;
    }

    public void AddFrm(String start, String end, String startTime, String arriveTime, String number, String cost,int index) {
        if(start.isEmpty()) addVertical(end,index);
        else  if(end.isEmpty()) addVertical(start,index);
        else {
            if(cost.isEmpty()) cost="0";
            addEdge(start,end,Integer.parseInt(cost),number,CoverToNUmber(startTime),getStayTime(CoverToNUmber(startTime),CoverToNUmber(arriveTime)),index);
        }
    }

    static class Count{
        public  Edge now;//存储当前所到地方
        public Queue<Edge> que= new LinkedList<>();//存储路径
        public int count;
        public Count(Edge e,int count){
            now=e;
            this.count=count;
        }
    }//辅助类，用以记录中转次数与路径
    //实现最快路径查找
    public String[][] DijkStra_Time(String start, String end,int indicate) {

        int pre = getIndex(start,indicate), en = getIndex(end,indicate);
        if (pre == -1 || en == -1) return null;
        downDate(indicate);
        int minCost, index ;//记录每次最少花费,以及下标
        int[] cost = new int[v_num[indicate]];//存储最少花费数目
        String time;//记录每次到站时间，继续寻找
        Edge tmp, min = null;//分别用来临时记录和取最小
        Edge[] e = new Edge[v_num[indicate] + 1];//存放路径
        boolean[] visited = new boolean[v_num[indicate] + 1];//记录是否为最短
        boolean flag = true;//最短路径寻找完毕

        //初始化数据
        for (int i = 0; i < v_num[indicate]; i++)
            if(pre!=i) cost[i] = Max;
        time = vertices[pre].next.start_time;
        visited[pre] = true;

        //dijkstra
        while (flag) {

            flag = false;
            minCost = Max;
            for (int i = 0; i < v_num[indicate]; i++) {
                if (!visited[i]) {
                    tmp = getMinVTime(vertices[pre].start, vertices[i].start, time,indicate);
                     if (tmp!=null&&cost[i] > cost[pre] + getStayTime(time, tmp.arrive_Time)) {
                        cost[i] = cost[pre] + getStayTime(time, tmp.arrive_Time);
                        e[i] = tmp;
                    }
                    flag = true;//继续循环
                }
            }
            for (int i = 0; i < v_num[indicate]; i++) {
                if (e[i]!=null&&!visited[i] && cost[i] < minCost) {
                    minCost = cost[i];
                    min = e[i];
                }
            }

            if (min != null) {
                index = getIndex(min.arrive,indicate);
            }
            else{
                break;
            }
            visited[index] = true;
            time = min.arrive_Time;
            pre = index;

        }
        //返回值以对应形式
        return getCovert(e,indicate,en);
    }

    public String[][] getCovert(Edge[] e,int index,int en){
        String[][] get = new String[v_num[index]][6];//存放时间、路径、车次，然后返回
        Edge edge=e[en];
        int k=0;
        while(edge!=null){
            k++;
            edge=e[getIndex(edge.start,index)];
        }
        edge=e[en];
        while(edge!=null){

            get[--k][0]=edge.start;
            get[k][1]=edge.arrive;
            get[k][2]=convertTo(edge.start_time);
            get[k][3]=convertTo(edge.arrive_Time);
            get[k][4]=edge.number;
            get[k][5]= String.valueOf(edge.cost);
            edge=e[getIndex(edge.start,index)];

        }
        if(get[0][0].isEmpty()) return null;
        else                 return get;
    }
    //实现最少中转查找
    public boolean isHasGet(Queue<Edge> que,String arrive){
        Queue<Edge> queue=new LinkedList<>();
        CopyOf(queue,que);
        while(!queue.isEmpty()){
            Edge tmp=queue.poll();
            if(tmp.arrive.equals(arrive)||tmp.start.equals(arrive))
                return false;
        }
        return true;
    }
    public void CopyOf(Queue<Edge> q1,Queue<Edge> q2){
        //q2复制给q1,保持q1，q2不变
        Edge[]  tmp=new Edge[q2.size()];
        int p=0;
        while (!q2.isEmpty()) {
            Edge t=q2.poll();
            q1.add(t);
            tmp[p++]=t;
        }
        q2.addAll(Arrays.asList(tmp));
    }
    private String[][] getCovertArray(Queue<Count> record) {
        Count minTurn = null;
        int min=Max;
        while (!record.isEmpty()){
            Count g=record.poll();
            if(min>g.count){
                minTurn=g;
                min=g.count;
                System.out.println(g.que.size()+"   "+g.count);
            }
        }//转换成需要的输出格式
        if(minTurn==null) return null;
        Queue<Edge> que=minTurn.que;
        System.out.println("size"+que.size());
        if(que.isEmpty()) return null;
        String[][] t=new String[que.size()][6];
        int i=0;
        while(!que.isEmpty())
        {
            Edge e=que.poll();
            t[i][0]=e.start;
            t[i][1]=e.arrive;
            t[i][2]=convertTo(e.start_time);
            t[i][3]=convertTo(e.arrive_Time);
            t[i][4]=e.number;
            t[i][5]= String.valueOf(e.cost);
            i++;
        }
        return t;
    }
    public String[][] minTurnNum(String start, String end,int index) {

        //广搜得所有可达路径,取最小的.
        int i=getIndex(start,index),j=getIndex(end,index);
        if(i==-1||j==-1) return null; //不存在，则直接返回null
        downDate(index);
        Edge d=vertices[i].next;//起始点
        Queue<Count> record= new LinkedList<>(); ///存储所有路径，遍历找中转最少
        Queue<Count> que= new LinkedList<>();//bfs队列
        boolean query=false;//是否找到路径
        Count tmp;//存储临时变量

        //初始化，把起点信息放入
        while(d!=null) {
            Count s = new Count(d, 0);
            s.que.add(d);
            que.add(s);
            d=d.next;
        }

        while (!que.isEmpty()){
            tmp=que.poll();
            if(tmp!=null&&tmp.now.arrive.equals(end)) {
                record.add(tmp);//完成搜索,存入可能路径
                query=true;
                continue;
            }
            Edge p= null;
            if (tmp != null)  p = vertices[getIndex(tmp.now.arrive,index)].next;//到达一个节点,从此结点出发找
            while(p!=null){
                if(isHasGet(tmp.que,p.arrive)&&tmp.now.number.equals(p.number)&&compar(tmp.now.arrive_Time,p.start_time)){
                    Count t= new Count(p, tmp.count);
                    CopyOf(t.que,tmp.que);
                    t.que.add(p);
                    que.add(t);
                }
                else if (isHasGet(tmp.que,p.arrive)&&compar(tmp.now.arrive_Time,p.start_time)&&!tmp.now.number.equals(p.number)){
                    Count t= new Count(p, tmp.count + 1);
                    CopyOf(t.que,tmp.que);
                    t.que.add(p);
                    que.add(t);
                }
                p=p.next;
            }
        }
        if(!query) return null;
        return getCovertArray(record);
    }

}
