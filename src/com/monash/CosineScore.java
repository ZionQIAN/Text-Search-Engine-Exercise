package com.monash;

import java.util.ArrayList;

// part of following code from stackoverflow

public class CosineScore {

    public double cosineSimilarity(Double[] docVector, Double[] queryVector) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < docVector.length; i++)
        {
            dotProduct += docVector[i] * queryVector[i];
            normA += Math.pow(docVector[i], 2);
            normB += Math.pow(queryVector[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
