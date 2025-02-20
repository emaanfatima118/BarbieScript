
# 🎀 BarbieScript 🎀

**BarbieScript** is a custom-designed programming language inspired by elegance, simplicity, and modernity. It introduces a stylish syntax while maintaining logical clarity, making programming fun and expressive.

---

##  Features
- **Lexical Analysis**: Supports identifiers, numbers, characters, keywords, and comments.
- **Data Types**: Supports integers, decimals, characters, and booleans.
- **Operators**: Arithmetic and assignment operators for calculations.
- **Special Keywords**: Custom-defined keywords for input, output, and constants.
- **Global and Local Variables**: Allows variable scope differentiation.
- **Elegant Syntax**: Intuitive and human-friendly syntax.

---

## Syntax Rules
| **Lexeme**                             | **Type**                |
|----------------------------------------|-------------------------|
| `[a-z]+`                               | IDENTIFIER              |
| `[0-9]`                                | INTEGER                 |
| `[a-z]`                                | CHARACTER               |
| `[0-9]+.[0-9]+`                        | DECIMAL                 |
| `sparkle, glossy, pinktruth, chatter`  | KEYWORD                 |
| `noway, sotrue`                        | BOOLEAN                 |
| `fix`                                  | CONSTANT                |
| `askqueen`                             | INPUT                   |
| `showoff`                              | OUTPUT                  |
| `#`                                    | ONE-LINE COMMENT        |
| `$/ ... /$`                            | MULTILINE COMMENT       |
| `queenbee`                             | GLOBAL VARIABLE         |
| `+, -, /, %, ^`                        | ARITHMETIC OPERATOR     |
| `=`                                    | ASSIGNMENT OPERATOR     |

---

## Installation & Setup
1. Clone the repository:
      ```sh

   git clone https://github.com/yourusername/BarbieScript.git
   
2. Navigate to the project directory:
      ```sh

   cd BarbieScript
   
3. Compile the BarbieScript Lexer and Parser (Java-based):
      ```sh

   javac BarbieScript.java
   
4. Run the interpreter:
      ```sh

   java BarbieScript example.bbs
   

---

## Example BarbieScript Code

    queenbee myVar = 10
    fix myConstant = 3.14
    askqueen userInput
    showoff "Hello, Barbie World!"


---

## Error Handling
- BarbieScript identifies rule violations and prints **error messages** with line numbers.
- It checks for **undefined variables**, **syntax errors**, and **invalid tokens**.

---

## Future Enhancements
- Add support for **functions and loops**.
- Extend **operator support** for more mathematical operations.
- Implement **a GUI-based BarbieScript Editor**.

---

## Contributing
We welcome contributions! If you’d like to improve BarbieScript:
1. Fork the repository.
2. Create a new branch: `git checkout -b feature-newFeature`
3. Commit changes: `git commit -m "Add new feature"`
4. Push to branch: `git push origin feature-newFeature`
5. Open a **Pull Request**.

---
