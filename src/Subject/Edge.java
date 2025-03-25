package Subject;

public class Edge {

    public String start;
    public String start_time;//出发时间
    public int time;//由出发时间和到达时间算出
    public int cost;
    public String arrive;
    public String number;
    public Edge next;

    public String arrive_Time;//到达时间

    public Edge(int cost,String arrive,String number,String start_time,int time,String start){
        this.start=start;
        this.start_time=start_time;
        this.cost=cost;
        this.arrive=arrive;
        this.number=number;
        next=null;
        if(start_time.isEmpty()) start_time="0";
        int arriveT=time/60*100+Integer.parseInt(start_time);
        arrive_Time=String.valueOf(arriveT);
    }
    public Edge(){
        next=null;
    }

    public String getTime(int  time,String start){

        //提取时、分
        int h;
        int min;
        if(start.length()==3) {
            h=Integer.parseInt(start.substring(0, 1));
            min=Integer.parseInt(start.substring(1,3));
        }
        else {
            h=Integer.parseInt(start.substring(0,2));
            min=Integer.parseInt(start.substring(2,4));
        }
        int tmp=min+time;
        if(tmp>=60){
            min=(tmp)%60;
            h+=tmp/60;
            if(h>=24)
                h-=24;
        }
        else{
            min=tmp;
        }
        return String.valueOf(h)+String.valueOf(min);
    }

}
