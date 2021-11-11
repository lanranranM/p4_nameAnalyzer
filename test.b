int a;
int b;
void func1(){
    a=0;
    a=b;
    ++a;
    ++b;
    receive >> a;
    print << b;
    if (a>0){
        a=1;
    }else{
        b=1;
        repeat (a){ a=b;}
    }
    while(b<100){
        ++b;
    }
    ret;
}
int func2 (int c, int d){
    ++c;
    ++d;
    ret;
}
int func3(int func3){
    ret;
}
int f(int a1, bool a2) {
    int x;
    int result;
    result = x + b;
    ++a;
    --b;
    result = a + b;
    a1 = b == x;
    result = a * b;
    result = a / 5;
    result = a2 - b;
    x = a;
    if (b) {
        ret x + 1;
    } else {
        ret x - 1;
    }
 }
 //struct
struct Point {
    int x;
    int y;
}

void g() {
    int a;
    bool b;
    struct Point p;
    p.x = a;
    b = a == 3;
    f(a + p.y*2, b);
    g();
}

void func4(){
    struct Point pt;
    pt.x = 7;
}
