LambdaPlus - Some simple extensions for Java8's Lambdas
---

Some simple extensions to add expressiveness to Java8's Lambda mechanism. So far, does two main things:
- introduces the 'Either' construct, which is either a 'Left' or a 'Right' and can be used for functional-style error-handling
- allows lifting of functions that throw exceptions to instead produce Either<Exception,Value>, allowing streams to be used with exception-throwing functions with minimal extra boilerplate

