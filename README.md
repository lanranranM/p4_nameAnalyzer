# P4

A simple name analyzer for `b` programming language.

Authors: Runduo Ma, Hankel Bao

Comp Sci 536

**How to invoke**

```
make
make test
```

**files  details**

- Sym.java: In this file, we define a subclass *FuncSym* to store the parameters list and return type of a function. We add new fields and getter methods for these two. Also, there is a override toString() method for unparsing.

  We also add new structTable & isStruct fields for the struct. We write a second constructor for struct case. Also, the getter methods for these two fields.

- SymTable.java: In this file, we define a new field: a HashMap to contains defined struct. Also, we add the getter, setter, add and lookup methods for the new structDefinedList.

- ast.java: we added nameAnalyzer() to nodes to check if the name is valid. Generally, if the current node is a declaration, we do some check call ProgramSymTable.addDecl. If the current node is a expression or statement, we do some check to see if it's a valid usage. For details, plz check the comments in the files.

- P4.java: the main program will parse, do nameAnalyzer, and unparse.

- test.b: conatins all valid name cases.

- nameErrors.b: contains error cases. 

