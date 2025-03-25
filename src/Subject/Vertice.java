package Subject;

public class Vertice {

    public String start;

    public Edge  next;
    public Edge end;

    public Vertice(String start){
        this.start=start;
         end=next=null;
    }
    public Vertice(){
        next=null;
    }

}
