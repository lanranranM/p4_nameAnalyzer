int a;
int b;
void func1(){
    a=0;
    a=b;
    ++a;
    ++b;
    receive >> a;
    print << b;
    if (a){
        a=1;
    }else{
        b=1;
        repeat (a){ a=b;}
    }
    while(b){
        ++b;
    }
    ret;
}