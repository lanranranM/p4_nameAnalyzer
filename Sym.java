import java.util.*;
import java.util.stream.Collectors;

public class Sym {
    protected String type;
    
    public Sym(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
    
    public String toString() {
        return type;
    }
}
//melo
class FuncSym extends Sym{
    private String retType;
    private int numParas;
    private List<String> paras;
    public FuncSym(String retType,List<String> paras){
        super("func");
        this.retType = retType;
        this.numParas = paras.size();
        this.paras = paras;
    }
    public String getRetType(){
        return this.retType;
    }
    public int getNumParas(){
        return this.numParas;
    }
    public List<String> getParas(){
        return this.paras;
    }
    public String toString(){
        String output = this.paras.stream().collect(Collectors.joining(", ")); 
        return output+ " -> " + this.retType;
    }
}
