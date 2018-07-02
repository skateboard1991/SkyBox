//
// Created by wujiaxin1 on 2018/6/22.
//
#include <jni.h>
#include <android/log.h>

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>
#include <cstring>
#include <iostream>
#ifndef CLIONPROJECTS_GLUTILS_H
#define CLIONPROJECTS_GLUTILS_H
using namespace std;
namespace glutils{
    int loadShader(string filePath,int type);
    int linkProgram(int vertexShader,int fragShader);
    int loadProgram(string vertexPath,string fragPath);
}
#endif //CLIONPROJECTS_GLUTILS_H
