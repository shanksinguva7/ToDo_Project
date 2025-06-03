# ✅ TODO Highlighter – IntelliJ Plugin

A custom IntelliJ plugin that highlights `TODO` comments in Kotlin files with different styles and displays them in a dedicated tool window.

## Features

- Highlights TODO comments with styles (bold, italic, colors)
- Shows all TODOs in a side panel for easy access
- Supports:
  - Single-line `// TODO:`
  - Block comments `/* TODO: ... */`
  - Inline TODOs in code
- The main and important files are named under "main" for easy knowledge 

## Project Structure

Kotlin_Project/
├── .idea/
├── src/
│ ├── Main.kt
│ ├── Testcase.kt
│ ├── TestcaseLarge.kt
├── .gitignore
├── TODO_Highlighter/ (plugin source code)
│ └── ...


## How to Use

1. Clone the repository: https://github.com/shanksinguva7/ToDo_Project.git
2. Open the project in IntelliJ IDEA.
3. Build or run the plugin from source.
4. Open any Kotlin file and click `Refresh TODOs` in the side panel.
5. View and jump to TODOs instantly.

## Testing

Use the provided Kotlin files to test the plugin:
- `Main.kt`
- `Testcase.kt`
- `TestcaseLarge.kt`

Each contains various types of TODO comments to test the plugin.

## Future Improvements

- Add support for other languages (Java, Python, etc.)
- Recognize other tags like `FIXME`, `NOTE`
- Filter/search options in the TODO side panel

## License

This project is licensed under the MIT License.

