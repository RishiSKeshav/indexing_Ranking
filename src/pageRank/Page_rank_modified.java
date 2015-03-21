
        mReachableVertices = reachableVertices;
        double numVertices = reachableVertices.size();
        mPreviousRankingsMap = new HashMap();
        mLeafNodes = new HashSet();
        for (Iterator vIt = mReachableVertices.iterator(); vIt.hasNext();) {
            Vertex currentVertex = (Vertex) vIt.next();
            setRankScore(currentVertex, 1.0 / numVertices);
            setPriorRankScore(currentVertex, 1.0 / numVertices);
            mPreviousRankingsMap.put(currentVertex, new MutableDouble(1.0 / numVertices));
            if (currentVertex.outDegree() == 0) {
                mLeafNodes.add(currentVertex);
            }
        }

        mUnreachableVertices = unreachableVertices;
        for (Iterator vIt = mUnreachableVertices.iterator(); vIt.hasNext();) {
            Vertex currentVertex = (Vertex) vIt.next();
            setRankScore(currentVertex, 0);
            setPriorRankScore(currentVertex, 0);
            mPreviousRankingsMap.put(currentVertex, new MutableDouble(0));
        }
    }

    protected void reinitialize() {
        initializeRankings(mReachableVertices, mUnreachableVertices);
    }

    protected void updateRankings() {
        double totalSum = 0;

        for (Iterator vIt = mReachableVertices.iterator(); vIt.hasNext();) {
            Vertex currentVertex = (Vertex) vIt.next();


            Set incomingEdges = currentVertex.getInEdges();

            double currentPageRankSum = 0;
            for (Iterator edgeIt = incomingEdges.iterator(); edgeIt.hasNext();) {
                Edge incomingEdge = (Edge) edgeIt.next();
                if (mUnreachableVertices.contains(incomingEdge.getOpposite(currentVertex)))
                    continue;

                double currentWeight = getEdgeWeight(incomingEdge);
                currentPageRankSum += ((Number) mPreviousRankingsMap.get(incomingEdge.getOpposite(currentVertex))).doubleValue() * currentWeight;
            }

            if (getPriorRankScore(currentVertex) > 0) {
                for (Iterator leafIt = mLeafNodes.iterator(); leafIt.hasNext();) {
                    Vertex leafNode = (Vertex) leafIt.next();
                    double currentWeight = getPriorRankScore(currentVertex);
                    currentPageRankSum += ((Number) mPreviousRankingsMap.get(leafNode)).doubleValue() * currentWeight;
                }
            }

           
            totalSum += currentPageRankSum * (1.0 - mAlpha) + mAlpha * getPriorRankScore(currentVertex);
            setRankScore(currentVertex, currentPageRankSum * (1.0 - mAlpha) + mAlpha * getPriorRankScore(currentVertex));
        }

        if (!NumericalPrecision.equal(totalSum, 1, .05)) {
            System.err.println("Page rank scores can not be generated because the specified graph is not connected.");
            System.out.println(totalSum);
        }
    }

    protected double evaluateIteration() {
        updateRankings();

        double rankingMSE = 0;

        //Normalize rankings and test for convergence
        for (Iterator vIt = mReachableVertices.iterator(); vIt.hasNext();) {
            Vertex currentVertex = (Vertex) vIt.next();
            MutableDouble previousRankScore = (MutableDouble) mPreviousRankingsMap.get(currentVertex);
            rankingMSE += Math.pow(getRankScore(currentVertex) - previousRankScore.doubleValue(), 2);
            previousRankScore.setDoubleValue(getRankScore(currentVertex));
        }

        rankingMSE = Math.pow(rankingMSE / getVertices().size(), 0.5);

        return rankingMSE;
    }

   
    public String getRankScoreKey() {
        return KEY;
    }

}