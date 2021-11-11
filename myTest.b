struct HungryLevel{
    int a;
}
struct Lanran{
    int b;
    struct HungryLevel h1;
}
void func5(){
    struct Lanran lanran1;
    lanran1.h1.a =1;
}
