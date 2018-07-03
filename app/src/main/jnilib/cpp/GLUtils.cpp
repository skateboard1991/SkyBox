//
// Created by wujiaxin1 on 2018/6/22.
//
#include <GLUtils/GLUtils.h>
#include <android/log.h>
#define  LOG_TAG    "skyboxjni"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
using namespace std;
namespace glutils {

    int loadShader(string shaderSource, int type) {
        const char *shaderSourceP = shaderSource.c_str();
        int shader = glCreateShader(type);
        if (!shader) {
            LOGE("create shader failed \n");
        }
        glShaderSource(shader, 1, &shaderSourceP, nullptr);
        glCompileShader(shader);
        int success = 0;
        GLchar infoLog[512];
        glGetShaderiv(shader, GL_COMPILE_STATUS, &success);
        if (!success) {
            glGetShaderInfoLog(shader, 512, nullptr, infoLog);
            LOGE("compile shader failed %s \n", infoLog);
        }
        return shader;
    }

    int linkProgram(int vertexShader, int fragShader) {
        int program = glCreateProgram();
        if (!program) {
            LOGE("crete program failed \n");
        }
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragShader);
        glLinkProgram(program);
        int success = 0;
        char infoLog[512];
        glGetProgramiv(program, GL_LINK_STATUS, &success);
        if (!success) {
            glGetProgramInfoLog(program, 512, nullptr, infoLog);
            LOGE("link program failed %s \n", infoLog);
        }
        glDeleteShader(vertexShader);
        glDeleteShader(fragShader);
        return program;
    }

    int loadProgram(string vertexShaderSource, string fragmentShaderSource) {
        int vertexShader = loadShader(vertexShaderSource, GL_VERTEX_SHADER);
        int fragShader = loadShader(fragmentShaderSource, GL_FRAGMENT_SHADER);
        int program = linkProgram(vertexShader, fragShader);
        return program;
    }
}



