# symbolic-postfix
Postfix parser with expression simplification and symbolic differentiation

This library parses postfix expressions into Abstract Syntax Trees and performs symbolic computations on them.
An instance of the SymbolicParser class takes an array of postfix tokens and returns an abstract syntax tree representing the
postfix expression. Every node of this tree is itself an Expression, and an Expression can be one of four types:

  - A known value, (e.g., 0, 1, 2.5, 19, -304.49)
  - A named constant or variable, (e.g., pi, x, e, r, i)
  - A function and an argument, (e.g., sin(expr), cos(expr), abs(expr))
  - An operation on two operands, (e.g., (op1 + op2), (op1 * op2), (op1 ^ op2))
  
  These four types are defined in classes that implement the Expression interface. This means that function arguments and operation operands
  can be anything, as they are also Expressions.
  
  The Expression interface specifies several methods that perform symbolic computations. 
  
  - Expressions can be simplified given a set of known constants. For example, the expression ((2 * 3) * ((x + 1) * (x + 1))) can be 
    simplified to (6 * ((x + 1) ^ 2)) if x is not known, and it can be simplified to 96 is x is set to 3, for instance. If no entry for
    x exists in the constants map, it will be left unsimplified; otherwise the value given for x will be used. The constants map also allows
    some constants to be defined so that they will never be evaluated as is; for example, i can be defined as (-1 ^ 0.5) and used in this form,
    because evaluating this expression as normal would produce NaN.
    
  - Expressions can also be evaluated given known constants, although this is a less powerful version of simplification that will throw
    an exception if the operation cannot be performed (for example, if a necessary variable is not defined).
    
  - Most importantly, Expressions can be differentiated with respect to any variable (even pi, if you choose to have it be a variable).
    The complexity of the Expression is irrelevant; like all other Expression operations, differentiation is implemented recursively
    and Expressions at lower levels on the tree are always differentiated first and built up to a final solution. The final solution is a
    symbolic derivative, and is also an Expression. Any expression can be differentiated; a general power rule for expressions of the form
    (f(x) ^ g(x)) is even included, in addition to all other rules (such as the chain rule).
  
  
 
The Expression interface also specifies toString(), which returns a readable version of the Expression (not in postfix syntax).

A toLatex() method is also specified, which returns a LaTeX representation of the Expression.

All subclasses of Expression are immutable; in fact, most of the data structures in this library are immutable, and the library is
designed so that most of the required functionality is specified in interfaces.
