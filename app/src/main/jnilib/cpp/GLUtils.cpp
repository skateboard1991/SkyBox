//
// Created by wujiaxin1 on 2018/6/22.
//
#include <GLUtils/GLUtils.h>
#include <iostream>
#include <fstream>
#include <sstream>
using namespace std;
namespace glutils {

    int loadShader(string filePath, int type) {
        ifstream file;
        string content;
        try {
            file.open(filePath, ios::in);
            ostringstream str;
            char ch;
            if (file.is_open()) {
                while (str && file.get(ch)) {
                    str.put(ch);
                }
            };
            file.close();
            content=str.str();
        }
        catch(std::ifstream::failure e)
        {
            std::cout << "ERROR::SHADER::FILE_NOT_SUCCESFULLY_READ" << std::endl;
        }
        const char *shaderSource = content.c_str();
        int shader = glCreateShader(type);
        if (!shader) {
            cout << "create shader failed" << endl;
        }
        glShaderSource(shader, 1, &shaderSource, nullptr);
        glCompileShader(shader);
        int success = 0;
        GLchar infoLog[512];
        glGetShaderiv(shader, GL_COMPILE_STATUS, &success);
        if (!success) {
            glGetShaderInfoLog(shader, 512, nullptr, infoLog);
            cout << "compile shader failed " << infoLog << endl;
        }
        return shader;
    }

    int linkProgram(int vertexShader, int fragShader) {
        int program = glCreateProgram();
        if (!program) {
            cout << "crete program failed" << endl;
        }
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragShader);
        glLinkProgram(program);
        int success = 0;
        char infoLog[512];
        glGetProgramiv(program, GL_LINK_STATUS, &success);
        if (!success) {
            glGetProgramInfoLog(program, 512, nullptr, infoLog);
            cout << "link program failed" << infoLog;
        }
        glDeleteShader(vertexShader);
        glDeleteShader(fragShader);
        return program;
    }

    int loadProgram(string vertexPath,string fragPath) {
        int vertexShader = loadShader(vertexPath, GL_VERTEX_SHADER);
        int fragShader = loadShader(fragPath, GL_FRAGMENT_SHADER);
        int program = linkProgram(vertexShader, fragShader);
        return program;
    }
}



