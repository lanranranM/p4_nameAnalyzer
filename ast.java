import java.io.*;
import java.util.*;

// **********************************************************************
// The ASTnode class defines the nodes of the abstract-syntax tree that
// represents a b program.
//
// Internal nodes of the tree contain pointers to children, organized
// either in a list (for nodes that may have a variable number of 
// children) or as a fixed set of fields.
//
// The nodes for literals and ids contain line and character number
// information; for string literals and identifiers, they also contain a
// string; for integer literals, they also contain an integer value.
//
// Here are all the different kinds of AST nodes and what kinds of children
// they have.  All of these kinds of AST nodes are subclasses of "ASTnode".
// Indentation indicates further subclassing:
//
//     Subclass            Kids
//     --------            ----
//     ProgramNode         DeclListNode
//     DeclListNode        linked list of DeclNode
//     DeclNode:
//       VarDeclNode       TypeNode, IdNode, int
//       FnDeclNode        TypeNode, IdNode, FormalsListNode, FnBodyNode
//       FormalDeclNode    TypeNode, IdNode
//       StructDeclNode    IdNode, DeclListNode
//
//     FormalsListNode     linked list of FormalDeclNode
//     FnBodyNode          DeclListNode, StmtListNode
//     StmtListNode        linked list of StmtNode
//     ExpListNode         linked list of ExpNode
//
//     TypeNode:
//       IntNode           -- none --
//       BoolNode          -- none --
//       VoidNode          -- none --
//       StructNode        IdNode
//
//     StmtNode:
//       AssignStmtNode      AssignNode
//       PreIncStmtNode     ExpNode
//       PreDecStmtNode     ExpNode
//       ReceiveStmtNode        ExpNode
//       PrintStmtNode       ExpNode
//       IfStmtNode          ExpNode, DeclListNode, StmtListNode
//       IfElseStmtNode      ExpNode, DeclListNode, StmtListNode,
//                                    DeclListNode, StmtListNode
//       WhileStmtNode       ExpNode, DeclListNode, StmtListNode
//       RepeatStmtNode      ExpNode, DeclListNode, StmtListNode
//       CallStmtNode        CallExpNode
//       ReturnStmtNode      ExpNode
//
//     ExpNode:
//       IntLitNode          -- none --
//       StrLitNode          -- none --
//       TrueNode            -- none --
//       FalseNode           -- none --
//       IdNode              -- none --
//       DotAccessNode       ExpNode, IdNode
//       AssignNode          ExpNode, ExpNode
//       CallExpNode         IdNode, ExpListNode
//       UnaryExpNode        ExpNode
//         UnaryMinusNode
//         NotNode
//       BinaryExpNode       ExpNode ExpNode
//         PlusNode     
//         MinusNode
//         TimesNode
//         DivideNode
//         AndNode
//         OrNode
//         EqualsNode
//         NotEqualsNode
//         LessNode
//         GreaterNode
//         LessEqNode
//         GreaterEqNode
//
// Here are the different kinds of AST nodes again, organized according to
// whether they are leaves, internal nodes with linked lists of kids, or
// internal nodes with a fixed number of kids:
//
// (1) Leaf nodes:
//        IntNode,   BoolNode,  VoidNode,  IntLitNode,  StrLitNode,
//        TrueNode,  FalseNode, IdNode
//
// (2) Internal nodes with (possibly empty) linked lists of children:
//        DeclListNode, FormalsListNode, StmtListNode, ExpListNode
//
// (3) Internal nodes with fixed numbers of kids:
//        ProgramNode,     VarDeclNode,     FnDeclNode,     FormalDeclNode,
//        StructDeclNode,  FnBodyNode,      StructNode,     AssignStmtNode,
//        PreIncStmtNode, PreDecStmtNode, ReceiveStmtNode,   PrintStmtNode   
//        IfStmtNode,      IfElseStmtNode,  WhileStmtNode,  RepeatStmtNode,
//        CallStmtNode
//        ReturnStmtNode,  DotAccessNode,   AssignExpNode,  CallExpNode,
//        UnaryExpNode,    BinaryExpNode,   UnaryMinusNode, NotNode,
//        PlusNode,        MinusNode,       TimesNode,      DivideNode,
//        AndNode,         OrNode,          EqualsNode,     NotEqualsNode,
//        LessNode,        GreaterNode,     LessEqNode,     GreaterEqNode
//
// **********************************************************************

// **********************************************************************
// <<<ASTnode class (base class for all other kinds of nodes)>>>
// **********************************************************************

abstract class ASTnode { 
    // every subclass must provide an unparse operation
    abstract public void unparse(PrintWriter p, int indent);

    // this method can be used by the unparse methods to do indenting
    protected void addIndent(PrintWriter p, int indent) {
        for (int k = 0; k < indent; k++) p.print(" ");
    }
}

// **********************************************************************
// <<<ProgramNode,  DeclListNode, FormalsListNode, FnBodyNode,
// StmtListNode, ExpListNode>>>
// **********************************************************************

class ProgramNode extends ASTnode {
    public ProgramNode(DeclListNode L) {
        myDeclList = L;
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
    }
    // melody
    public void nameAnalyzer(){
        SymTable program = new SymTable();
        myDeclList.nameAnalyzer(program);
        // for tracing - del later
        //System.out.println("program.nameAnalyzer");
    }
    // melody

    // 1 kid
    private DeclListNode myDeclList;
}

class DeclListNode extends ASTnode {
    public DeclListNode(List<DeclNode> S) {
        myDecls = S;
    }
    public void unparse(PrintWriter p, int indent) {
        Iterator it = myDecls.iterator();
        try {
            while (it.hasNext()) {
                ((DeclNode)it.next()).unparse(p, indent);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException in DeclListNode.print");
            System.exit(-1);
        }
    }
    // melo
    public void nameAnalyzer(SymTable program){
        Iterator it = myDecls.iterator();
        while (it.hasNext()){
            ((DeclNode)it.next()).nameAnalyzer(program);
        }
        // for tracing - del later
        //System.out.println("declListNode.nameAnalyzer");
    }
    //

    // list of kids (DeclNodes)
    private List<DeclNode> myDecls;
}

class FormalsListNode extends ASTnode {
    public FormalsListNode(List<FormalDeclNode> S) {
        myFormals = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<FormalDeclNode> it = myFormals.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) {  // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        } 
    }
    //melo
    public void nameAnalyzer(SymTable program){
        Iterator it = myFormals.iterator();
        while (it.hasNext()){
            ((FormalDeclNode)it.next()).nameAnalyzer(program);
        }
        // for tracing - del later
        //System.out.println("FormalsListNode.nameAnalyzer");
    }
    public List<String> getList(){
        List<String> output = new LinkedList<>();
        for(FormalDeclNode a: myFormals){
            output.add(a.getType());
        }
        return output;
    }
    //
    // list of kids (FormalDeclNodes)
    private List<FormalDeclNode> myFormals;
}

class FnBodyNode extends ASTnode {
    public FnBodyNode(DeclListNode declList, StmtListNode stmtList) {
        myDeclList = declList;
        myStmtList = stmtList;
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
        myStmtList.unparse(p, indent);
    }

    public void nameAnalyzer(SymTable program){
        myDeclList.nameAnalyzer(program);
        myStmtList.nameAnalyzer(program);
    }
    // 2 kids
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class StmtListNode extends ASTnode {
    public StmtListNode(List<StmtNode> S) {
        myStmts = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<StmtNode> it = myStmts.iterator();
        while (it.hasNext()) {
            it.next().unparse(p, indent);
        }
    }
    // melo
    public void nameAnalyzer(SymTable program){
        Iterator<StmtNode> it = myStmts.iterator();
        while (it.hasNext()) {
            it.next().nameAnalyzer(program);
        }
    }
    // melo

    // list of kids (StmtNodes)
    private List<StmtNode> myStmts;
}

class ExpListNode extends ASTnode {
    public ExpListNode(List<ExpNode> S) {
        myExps = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<ExpNode> it = myExps.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) {  // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        } 
    }
    // melo
    public void nameAnalyzer(SymTable program){
        Iterator<ExpNode> it = myExps.iterator();
        while (it.hasNext()) {
            it.next().nameAnalyzer(program);
        }
    }
    // melo
    // list of kids (ExpNodes)
    private List<ExpNode> myExps;
}

// **********************************************************************
// <<<DeclNode and its subclasses>>>
// **********************************************************************

abstract class DeclNode extends ASTnode {
    //melo
    abstract public void nameAnalyzer(SymTable program);
}

class VarDeclNode extends DeclNode {
    public VarDeclNode(TypeNode type, IdNode id, int size) {
        myType = type;
        myId = id;
        mySize = size;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
        p.println(";");
    }
    // melo
    public void nameAnalyzer(SymTable program){
        // case 1: varDecl
        // case 2??? structDecl
        if(myType.getType().equals("void")){
            String msg = "Non-function declared void";
            ErrMsg.fatal(myId.getLine(), myId.getChar(), msg);
            return;
        }
        if(mySize==-1){
            try {
                program.addDecl(myId.getID(),new Sym(myType.getType()));
            } catch (DuplicateSymException e) {
                String msg = "Multiply declared identifier";
                ErrMsg.fatal(myId.getLine(), myId.getChar(), msg);
            } catch (Exception e){
                System.out.println(e);
            }
            
        }
        else{
            // check if defined & multiple declared 
            Sym struct = program.lookupStruct(myType.getType());
            //System.out.println("before");
            //program.print();
            //program.printStr();
            if(struct==null){
                //System.out.println(myType.getType());
                String msg = "Invalid name of struct type";
                ErrMsg.fatal(myId.getLine(), myId.getChar(), msg);
                return;
            }else{
                try{
                    program.addDecl(myId.getID(), struct);
                }catch (DuplicateSymException e) {
                    String msg = "Multiply declared identifier";
                    ErrMsg.fatal(myId.getLine(), myId.getChar(), msg);
                } catch (Exception e){
                    System.out.println(e);
                }
            }
            //System.out.println("after");
            //program.print();
            //program.printStr();
        }
        // for tracing - del later
        //System.out.println("var.nameAnalyzer");
    }
    // melo

    // 3 kids
    private TypeNode myType;
    private IdNode myId;
    private int mySize;  // use value NOT_STRUCT if this is not a struct type

    public static int NOT_STRUCT = -1;
}

class FnDeclNode extends DeclNode {
    public FnDeclNode(TypeNode type,
                      IdNode id,
                      FormalsListNode formalList,
                      FnBodyNode body) {
        myType = type;
        myId = id;
        myFormalsList = formalList;
        myBody = body;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
        p.print("(");
        myFormalsList.unparse(p, 0);
        p.println(") {");
        myBody.unparse(p, indent+4);
        p.println("}\n");
    }
    //melo todo! err case
    public void nameAnalyzer(SymTable program){  
        try {
            program.addDecl(myId.getID(), new FuncSym(myType.getType(),myFormalsList.getList()));
        } catch (DuplicateSymException e) {
            String msg = "Multiply declared identifier";
            ErrMsg.fatal(myId.getLine(), myId.getChar(), msg);
        } catch (Exception e){
            System.out.println(e);
        }
            
        program.addScope();
        myFormalsList.nameAnalyzer(program);
        myBody.nameAnalyzer(program);
        try {
            program.removeScope();
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    //melo

    // 4 kids
    private TypeNode myType;
    private IdNode myId;
    private FormalsListNode myFormalsList;
    private FnBodyNode myBody;
}

class FormalDeclNode extends DeclNode {
    public FormalDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
    }
    //melo 
    public void nameAnalyzer(SymTable program){
        try {
            program.addDecl(myId.getID(), new Sym(myType.getType()));
        } catch (DuplicateSymException e) {
            String msg = "Multiply declared identifier";
            ErrMsg.fatal(myId.getLine(), myId.getChar(), msg);
        } catch (Exception e){
            System.out.println(e);
        }
    }
    public String getType(){
        return myType.getType();
    }
    //melo
    // 2 kids
    private TypeNode myType;
    private IdNode myId;
}

class StructDeclNode extends DeclNode {
    public StructDeclNode(IdNode id, DeclListNode declList) {
        myId = id;
        myDeclList = declList;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("struct ");
        myId.unparse(p, 0);
        p.println("{");
        myDeclList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("};\n");

    }
    //melo
    public void nameAnalyzer(SymTable program){
        // create a new sym and add it to structDefinedList
        //System.out.println("defining a struct: "+myId.getID());
        Sym struct = new Sym(myId.getID(),true);
        SymTable structScope = struct.getStructTable();
        structScope.setStructDefinedList(program.getStructDefinedList());
        try {
            program.addStruct(myId.getID(),struct);
        } catch (DuplicateSymException e) {
            String msg = "Multiply declared identifier";
            ErrMsg.fatal(myId.getLine(), myId.getChar(), msg);
            return;
        }
        
        //structScope.addScope();
        myDeclList.nameAnalyzer(structScope);
    }
    //melo

    // 2 kids
    private IdNode myId;
    private DeclListNode myDeclList;
}

// **********************************************************************
// <<<TypeNode and its Subclasses>>>
// **********************************************************************

abstract class TypeNode extends ASTnode {
    //melo
    abstract public String getType();
    //
}

class IntNode extends TypeNode {
    public IntNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("int");
    }
    //melo
    public String getType(){
        return "int";
    }
    //melo
}

class BoolNode extends TypeNode {
    public BoolNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("bool");
    }
    //melo
    public String getType(){
        return "bool";
    }
    //melo
}

class VoidNode extends TypeNode {
    public VoidNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("void");
    }
    //melo
    public String getType(){
        return "void";
    }
    //melo
}

class StructNode extends TypeNode {
    public StructNode(IdNode id) {
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("struct ");
        myId.unparse(p, 0);
    }
    //melo
    public String getType(){
        return myId.getID(); //todo only idName or with struct?
    }
    //melo
    // 1 kid
    private IdNode myId;
}

// **********************************************************************
// <<<StmtNode and its subclasses>>>
// **********************************************************************

abstract class StmtNode extends ASTnode {
    //melo
    abstract public void nameAnalyzer(SymTable program);
    //
}

class AssignStmtNode extends StmtNode {
    public AssignStmtNode(AssignNode assign) {
        myAssign = assign;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myAssign.unparse(p, -1); // no parentheses
        p.println(";");
    }
    // melo
    public void nameAnalyzer(SymTable program){
        myAssign.nameAnalyzer(program);
    }
    // melo
    // 1 kid
    private AssignNode myAssign;
}

class PreIncStmtNode extends StmtNode {
    public PreIncStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
	    p.print("++");
        myExp.unparse(p, 0);
        p.println(";");
    }
    // melo
    public void nameAnalyzer(SymTable program){
        myExp.nameAnalyzer(program);
    }
    // melo
    // 1 kid
    private ExpNode myExp;
}

class PreDecStmtNode extends StmtNode {
    public PreDecStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
	p.print("--");
        myExp.unparse(p, 0);
        p.println(";");
    }
    // melo
    public void nameAnalyzer(SymTable program){
        myExp.nameAnalyzer(program);
    }
    // melo
    // 1 kid
    private ExpNode myExp;
}

class ReceiveStmtNode extends StmtNode {
    public ReceiveStmtNode(ExpNode e) {
        myExp = e;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("receive >> ");
        myExp.unparse(p, 0);
        p.println(";");
    }
    // melo
    public void nameAnalyzer(SymTable program){
        myExp.nameAnalyzer(program);
    }
    // melo
    // 1 kid (actually can only be an IdNode or an ArrayExpNode)
    private ExpNode myExp;
}

class PrintStmtNode extends StmtNode {
    public PrintStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("print << ");
        myExp.unparse(p, 0);
        p.println(";");
    }
    // melo
    public void nameAnalyzer(SymTable program){
        myExp.nameAnalyzer(program);
    }
    // melo
    // 1 kid
    private ExpNode myExp;
}

class IfStmtNode extends StmtNode {
    public IfStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myDeclList = dlist;
        myExp = exp;
        myStmtList = slist;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");
    }
    // melo
    public void nameAnalyzer(SymTable program){
        myExp.nameAnalyzer(program);
        program.addScope();
        myDeclList.nameAnalyzer(program);
        myStmtList.nameAnalyzer(program);
        try{
            program.removeScope();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    // melo
    // e kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class IfElseStmtNode extends StmtNode {
    public IfElseStmtNode(ExpNode exp, DeclListNode dlist1,
                          StmtListNode slist1, DeclListNode dlist2,
                          StmtListNode slist2) {
        myExp = exp;
        myThenDeclList = dlist1;
        myThenStmtList = slist1;
        myElseDeclList = dlist2;
        myElseStmtList = slist2;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myThenDeclList.unparse(p, indent+4);
        myThenStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");
        addIndent(p, indent);
        p.println("else {");
        myElseDeclList.unparse(p, indent+4);
        myElseStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");        
    }
    // melo
    public void nameAnalyzer(SymTable program){
        myExp.nameAnalyzer(program);
        program.addScope();
        myThenDeclList.nameAnalyzer(program);
        myThenStmtList.nameAnalyzer(program);
        try{
            program.removeScope();
        }catch (Exception e){
            System.out.println(e);
        }
        program.addScope();
        myElseStmtList.nameAnalyzer(program);
        myElseDeclList.nameAnalyzer(program);
        try{
            program.removeScope();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    // melo
    // 5 kids
    private ExpNode myExp;
    private DeclListNode myThenDeclList;
    private StmtListNode myThenStmtList;
    private StmtListNode myElseStmtList;
    private DeclListNode myElseDeclList;
}

class WhileStmtNode extends StmtNode {
    public WhileStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }
    
    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("while (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");
    }
    // melo
    public void nameAnalyzer(SymTable program){
        myExp.nameAnalyzer(program);
        program.addScope();
        myDeclList.nameAnalyzer(program);
        myStmtList.nameAnalyzer(program);
        try{
            program.removeScope();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    // melo
    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class RepeatStmtNode extends StmtNode {
    public RepeatStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }
	
    public void unparse(PrintWriter p, int indent) {
	addIndent(p, indent);
        p.print("repeat (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");
    }
    // melo
    public void nameAnalyzer(SymTable program){
        myExp.nameAnalyzer(program);
        program.addScope();
        myDeclList.nameAnalyzer(program);
        myStmtList.nameAnalyzer(program);
        try{
            program.removeScope();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    // melo
    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class CallStmtNode extends StmtNode {
    public CallStmtNode(CallExpNode call) {
        myCall = call;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myCall.unparse(p, indent);
        p.println(";");
    }
    // melo
    public void nameAnalyzer(SymTable program){
        myCall.nameAnalyzer(program);
    }
    //
    // 1 kid
    private CallExpNode myCall;
}

class ReturnStmtNode extends StmtNode {
    public ReturnStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("return");
        if (myExp != null) {
            p.print(" ");
            myExp.unparse(p, 0);
        }
        p.println(";");
    }
    //melo
    public void nameAnalyzer(SymTable program){
        if(myExp!=null)
            myExp.nameAnalyzer(program);
    }
    //
    // 1 kid
    private ExpNode myExp; // possibly null
}

// **********************************************************************
// <<<ExpNode and its subclasses>>>
// **********************************************************************

abstract class ExpNode extends ASTnode {
    abstract public SymTable nameAnalyzer(SymTable program);
}

class IntLitNode extends ExpNode {
    public IntLitNode(int lineNum, int charNum, int intVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myIntVal = intVal;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myIntVal);
    }
    //melo
    public SymTable nameAnalyzer(SymTable program){return null;}
    //

    private int myLineNum;
    private int myCharNum;
    private int myIntVal;
}

class StringLitNode extends ExpNode {
    public StringLitNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
    }
    //melo
    public SymTable nameAnalyzer(SymTable program){return null;}
    //
    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}

class TrueNode extends ExpNode {
    public TrueNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("true");
    }
    //melo
    public SymTable nameAnalyzer(SymTable program){return null;}
    //
    private int myLineNum;
    private int myCharNum;
}

class FalseNode extends ExpNode {
    public FalseNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("false");
    }
    //melo
    public SymTable nameAnalyzer(SymTable program){return null;}
    //
    private int myLineNum;
    private int myCharNum;
}

class IdNode extends ExpNode {
    public IdNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
        if (link!=null)
            p.print("("+link.toString()+")");
    }

    // melo
    public int getLine(){
        return myLineNum;
    }
    public int getChar(){
        return myCharNum;
    }
    public String getID(){
        return myStrVal;
    }
    public Sym getSym(){
        return link;
    }
    public void setLink(Sym link){
        this.link = link;
    }
    public SymTable nameAnalyzer(SymTable program){
        // structNode?
        link = program.lookupGlobal(myStrVal);
        if(link == null){
            String msg = "Undeclared identifier";
            ErrMsg.fatal(myLineNum, myCharNum, msg);
            return null;
        }
        if(link.checkIsStruct()){
            return link.getStructTable();
        }
        return null;
    }
    // melo
    private Sym link; //melo
    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}

class DotAccessExpNode extends ExpNode {
    public DotAccessExpNode(ExpNode loc, IdNode id) {
        myLoc = loc;    
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myLoc.unparse(p, 0);
        p.print(").");
        myId.unparse(p, 0);
    }
    //melo
    public SymTable nameAnalyzer(SymTable program){
        // check if lhs is a declared struct
        // check if rhs is a valid field
        SymTable structTable = myLoc.nameAnalyzer(program);
        if(structTable == null && (myLoc instanceof IdNode)){
            String msg = "Dot-access of non-struct type";
            ErrMsg.fatal(((IdNode)myLoc).getLine(), ((IdNode)myLoc).getChar(), msg);
            return null;
        }
        Sym link = structTable.lookupGlobal(myId.getID());
        if(link == null){
            String msg = "Invalid struct field name";
            ErrMsg.fatal(myId.getLine(), myId.getChar(), msg);
        }
        myId.setLink(link);
        if(link!=null)
            return link.getStructTable();
        return null;
    }
    //melo
    // 2 kids
    private ExpNode myLoc;    
    private IdNode myId;
}

class AssignNode extends ExpNode {
    public AssignNode(ExpNode lhs, ExpNode exp) {
        myLhs = lhs;
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        if (indent != -1)  p.print("(");
        myLhs.unparse(p, 0);
        p.print(" = ");
        myExp.unparse(p, 0);
        if (indent != -1)  p.print(")");
    }
    // melo
    public SymTable nameAnalyzer(SymTable program){
        myLhs.nameAnalyzer(program);
        myExp.nameAnalyzer(program);
        return null;
    }
    // melo

    // 2 kids
    private ExpNode myLhs;
    private ExpNode myExp;
}

class CallExpNode extends ExpNode {
    public CallExpNode(IdNode name, ExpListNode elist) {
        myId = name;
        myExpList = elist;
    }

    public CallExpNode(IdNode name) {
        myId = name;
        myExpList = new ExpListNode(new LinkedList<ExpNode>());
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
        myId.unparse(p, 0);
        p.print("(");
        if (myExpList != null) {
            myExpList.unparse(p, 0);
        }
        p.print(")");
    }
    //melo
    public SymTable nameAnalyzer(SymTable program){
        myId.nameAnalyzer(program);
        myExpList.nameAnalyzer(program);
        return null;
    }
    //
    // 2 kids
    private IdNode myId;
    private ExpListNode myExpList;  // possibly null
}

abstract class UnaryExpNode extends ExpNode {
    public UnaryExpNode(ExpNode exp) {
        myExp = exp;
    }
    //melo
    public SymTable nameAnalyzer(SymTable program){
        myExp.nameAnalyzer(program);
        return null;
    }
    //
    // one child
    protected ExpNode myExp;
}

abstract class BinaryExpNode extends ExpNode {
    public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
        myExp1 = exp1;
        myExp2 = exp2;
    }
    //melo
    public SymTable nameAnalyzer(SymTable program){
        myExp1.nameAnalyzer(program);
        myExp2.nameAnalyzer(program);
        return null;
    }
    //
    // two kids
    protected ExpNode myExp1;
    protected ExpNode myExp2;
}

// **********************************************************************
// <<<Subclasses of UnaryExpNode>>>
// **********************************************************************

class UnaryMinusNode extends UnaryExpNode {
    public UnaryMinusNode(ExpNode exp) {
        super(exp);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(-");
        myExp.unparse(p, 0);
        p.print(")");
    }
}

class NotNode extends UnaryExpNode {
    public NotNode(ExpNode exp) {
        super(exp);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(!");
        myExp.unparse(p, 0);
        p.print(")");
    }
}

// **********************************************************************
// <<<Subclasses of BinaryExpNode>>>
// **********************************************************************

class PlusNode extends BinaryExpNode {
    public PlusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" + ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class MinusNode extends BinaryExpNode {
    public MinusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" - ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class TimesNode extends BinaryExpNode {
    public TimesNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" * ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class DivideNode extends BinaryExpNode {
    public DivideNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" / ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class AndNode extends BinaryExpNode {
    public AndNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" && ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class OrNode extends BinaryExpNode {
    public OrNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" || ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class EqualsNode extends BinaryExpNode {
    public EqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" == ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class NotEqualsNode extends BinaryExpNode {
    public NotEqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" != ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class LessNode extends BinaryExpNode {
    public LessNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" < ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class GreaterNode extends BinaryExpNode {
    public GreaterNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" > ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class LessEqNode extends BinaryExpNode {
    public LessEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" <= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class GreaterEqNode extends BinaryExpNode {
    public GreaterEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" >= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}
