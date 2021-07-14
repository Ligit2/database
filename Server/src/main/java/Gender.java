import java.util.Random;

public enum Gender {
    MALE,
    FEMALE;
    public static Gender getRandomGender(){
        Random random = new Random();
        if(random.nextInt(100)%2==0)return MALE;
        else return FEMALE;
    }
}
