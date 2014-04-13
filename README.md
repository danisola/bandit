# Bandit Algorithms

Inspired by [Bandit Algorithms for Website Optimization](http://shop.oreilly.com/product/0636920027393.do) I've implemented some bandit algorithms in Java and a test framework to quickly visualize how they behave under different scenarios.

Currently, the algorithms available are: [epsilon-Greedy](https://github.com/danisola/bandit/blob/master/algorithms/src/main/java/com/danisola/bandit/EpsilonGreedyAlgorithm.java), [epsilon-first](https://github.com/danisola/bandit/blob/master/algorithms/src/main/java/com/danisola/bandit/EpsilonFirstAlgorithm.java), [Softmax](https://github.com/danisola/bandit/blob/master/algorithms/src/main/java/com/danisola/bandit/SoftmaxAlgorithm.java) and [UCB1](https://github.com/danisola/bandit/blob/master/algorithms/src/main/java/com/danisola/bandit/Ucb1Algorithm.java). 

![Test Framework](http://danisola.github.io/bandit/imgs/test-framework.png)


## How to use the test framework

If you want to change the algorithms or arms used in the test, download the code and configure the test in [Main.java](https://github.com/danisola/bandit/blob/master/test-framework/src/main/java/com/danisola/bandit/testframework/Main.java)


## How to build

You require the following:

* Latest stable JDK 8
* Latest stable Apache Maven

Download the code and do `mvn clean package`. To run the test framework just do `java -jar test-framework/target/testframework.jar`.
