import java.util.*;

public class SymTable {
    private List<HashMap<String, Sym>> list;
    //melo
    private HashMap<String, Sym> structDefinedList;
    //
    
    public SymTable() {
        list = new LinkedList<HashMap<String, Sym>>();
        list.add(new HashMap<String, Sym>());
        structDefinedList = new HashMap<String, Sym>();
    }
    //melo
    public HashMap<String, Sym> getStructDefinedList(){
        return this.structDefinedList;
    }
    public void setStructDefinedList(HashMap<String, Sym> OldStructDefinedList){
        this.structDefinedList = OldStructDefinedList;
    }
    public void addStruct(String name, Sym sym) throws DuplicateSymException{
        if (structDefinedList.containsKey(name)||this.lookupGlobal(name)!=null){
            throw new DuplicateSymException();
        }
        structDefinedList.put(name,sym);
    }
    public Sym lookupStruct(String name){
        if (structDefinedList.isEmpty())
            return null;
        return structDefinedList.get(name);
    }
    //
    
    public void addDecl(String name, Sym sym) 
	throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
	if (name == null && sym == null) {
	    throw new WrongArgumentException("Arguments name and sym are null.");
	}
	else if (name == null) {
	    throw new WrongArgumentException("Argument name is null.");
	}
	else if (sym == null) {
	    throw new WrongArgumentException("Argument sym is null.");
	}
               
        if (list.isEmpty()) {
            throw new EmptySymTableException();
        }
	
        HashMap<String, Sym> symTab = list.get(0);
        if (symTab.containsKey(name)||structDefinedList.containsKey(name))
            throw new DuplicateSymException();
        // if (symTab.containsKey(name))
        //     throw new DuplicateSymException();
        
        symTab.put(name, sym);
    }
    
    public void addScope() {
        list.add(0, new HashMap<String, Sym>());
    }
    
    public Sym lookupLocal(String name) {
        if (list.isEmpty())
            return null;
        
        HashMap<String, Sym> symTab = list.get(0); 
        return symTab.get(name);
    }
    
    public Sym lookupGlobal(String name) {
        if (list.isEmpty())
            return null;
        
        for (HashMap<String, Sym> symTab : list) {
            Sym sym = symTab.get(name);
            if (sym != null)
                return sym;
        }
        return null;
    }
    
    public void removeScope() throws EmptySymTableException {
        if (list.isEmpty())
            throw new EmptySymTableException();
        list.remove(0);
    }
    
    public void print() {
        System.out.print("\n=== Sym Table ===\n");
        for (HashMap<String, Sym> symTab : list) {
            System.out.println(symTab.toString());
        }
        System.out.println();
    }
    //melo
    public void printStr() {
        System.out.print("\n=== SymStr Table ===\n");
        System.out.println(structDefinedList.toString());
        System.out.println();
    }
    //
}
