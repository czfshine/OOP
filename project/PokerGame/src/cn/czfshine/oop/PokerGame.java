package cn.czfshine.oop;
import javax.sound.midi.Soundbank;
import java.util.*;

public class PokerGame {
    static int RenShu,PaiShu;
    static void AddYiFuPai(List<Pai> L){
        for (int i=0;i<4;i++){
            for(int j=0;j<13;j++){
                L.add(new Pai(j,i));
            }
        }
    }
    static List<Pai>  QiePai(){
        List<Pai> PaiSet=new ArrayList<Pai>();
        for(int i=0;i<PaiShu;i++){
            AddYiFuPai(PaiSet);
        }
        Collections.shuffle(PaiSet);
        return PaiSet;
    }
    static List<List<Pai>> FaPai(List<Pai> L){
        List<List<Pai>> Ren=new ArrayList<>();
        for(int i=0;i<RenShu;i++){
            Ren.add(new ArrayList<>());
        }

        for(int i=0;i<PaiShu*52;i++){
            Ren.get(i%RenShu).add(L.get(i));//Todo
        }


        return  Ren;

    }
    static void Print(List<Pai> L){
        String[] h={"黑桃","红心","草花","方块"};
        String[] num={"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        L.sort((o1, o2) -> {
            Pai o=(Pai)o1;
            Pai u=(Pai)o2;
            return (o.huase*1000+o.num) - (u.huase*1000+u.num);
        });
        int past=0;
        System.out.printf("\n%s:",h[0]);
        for (int i=0;i<L.size();i++){
            Pai p=L.get(i);
            if(p.huase!=past){
                System.out.printf("\n%s:",h[p.huase]);
                past=p.huase;
            }
            System.out.printf(" %s",num[p.num]);
        }
        System.out.println();
    }
    public static void main(String[] args) {
        Scanner input=new Scanner(System.in);

        System.out.println("人数：");
        RenShu=input.nextInt();
        System.out.println("牌数：");
        PaiShu=input.nextInt();
        List<Pai> PaiSet=QiePai();
        List<List<Pai>> Ren=FaPai(PaiSet);
        for (int i=0;i<RenShu;i++){
            System.out.printf("第%d个人",i+1);
            Print(Ren.get(i));
        }



    }
}
