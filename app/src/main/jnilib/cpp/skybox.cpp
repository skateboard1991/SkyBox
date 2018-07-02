#include <jni.h>
#include <string>
#include <GLUtils/GLUtils.h>
#include <GLUtils/stb_image.h>

extern "C" {
JNIEXPORT jstring JNICALL Java_com_skateboard_skybox_SkyBoxRender_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


JNIEXPORT jint JNICALL Java_com_skateboard_skybox_SkyBoxRender_genProgram(JNIEnv *env, jobject thiz, jstring vertexPath, jstring fragmentPath) {
    //load program
    const char *cVertexPath = env->GetStringUTFChars(vertexPath, nullptr);
    const char *cFragmentPath = env->GetStringUTFChars(fragmentPath, nullptr);
    int program = glutils::loadProgram(cVertexPath, cFragmentPath);
    return program;

}

JNIEXPORT jint JNICALL Java_com_skateboard_skybox_SkyBoxRender_preparePos(JNIEnv *env, jobject thiz, jfloatArray pos) {
    //gen vao vbo
    unsigned int VAO, VBO;
    glGenVertexArrays(1, &VAO);
    glBindVertexArray(VAO);
    glGenBuffers(1, &VBO);
    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(pos), pos, GL_STATIC_DRAW);
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), 0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);


    return VAO;
}

JNIEXPORT jint JNICALL Java_com_skateboard_skybox_SkyBoxRender_prepareTexture(JNIEnv *env, jobject thiz) {
    //gen texture
    unsigned int TEXTURE;
    glGenTextures(1, &TEXTURE);
    glBindTexture(GL_TEXTURE_CUBE_MAP, TEXTURE);
    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
    return 1;
}

JNIEXPORT void JNICALL Java_com_skateboard_skybox_SkyBoxRender_draw(JNIEnv *env, jobject thiz, jint program, jint VAO) {
    glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
    glUseProgram(program);
    glBindVertexArray(VAO);
    glDrawArrays(GL_TRIANGLES, 0, 36);
}
}

