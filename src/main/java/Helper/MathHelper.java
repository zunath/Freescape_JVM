package Helper;

public class MathHelper {

    public static int GetRandomWeightedIndex(int[] weights)
    {
        int totalWeight = 0;
        for(int weight : weights)
        {
            totalWeight += weight;
        }

        int randomIndex = -1;
        double random = Math.random() * totalWeight;
        for(int i = 0; i < weights.length; ++i)
        {
            random -= weights[i];
            if(random <= 0.0d)
            {
                randomIndex = i;
                break;
            }
        }

        return randomIndex;
    }

}
