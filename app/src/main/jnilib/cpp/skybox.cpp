#include <jni.h>
#include <string>
#include <GLUtils/GLUtils.h>
#include <glm/glm.hpp>
#include <glm/gtc/type_ptr.hpp>
#include <glm/gtc/matrix_transform.hpp>

extern "C" {

JNIEXPORT jint JNICALL
Java_com_skateboard_skybox_SkyBoxRender_genProgram(JNIEnv *env, jobject thiz, jstring vertexPath,
                                                   jstring fragmentPath) {
    //load program
    const char *cVertexPath = env->GetStringUTFChars(vertexPath, nullptr);
    const char *cFragmentPath = env->GetStringUTFChars(fragmentPath, nullptr);
    int program = glutils::loadProgram(cVertexPath, cFragmentPath);
    return program;

}

JNIEXPORT jint JNICALL
Java_com_skateboard_skybox_SkyBoxRender_preparePos(JNIEnv *env, jobject thiz, jfloatArray pos) {
    //gen vao vbo

    unsigned int VAO, VBO;
    glGenVertexArrays(1, &VAO);
    glBindVertexArray(VAO);
    glGenBuffers(1, &VBO);
    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    int posSize = env->GetArrayLength(pos);
    float* p=env->GetFloatArrayElements(pos, nullptr);
    glBufferData(GL_ARRAY_BUFFER, posSize* sizeof(float), p,
                 GL_STATIC_DRAW);
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), 0);
    glBindVertexArray(0);
    return VAO;
}

JNIEXPORT jint JNICALL
Java_com_skateboard_skybox_SkyBoxRender_prepareTexture(JNIEnv *env, jobject thiz) {
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

glm::vec3 cameraPos = glm::vec3(0.0f, 0.0f, 0.0f);
glm::vec3 cameraFront = glm::vec3(0.0f, 0.0f, -1.0f);

JNIEXPORT void JNICALL
Java_com_skateboard_skybox_SkyBoxRender_draw(JNIEnv *env, jobject thiz, jint program, jint VAO,
                                             jint texture,jfloat width,jfloat height) {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glClearColor(0.0, 1.0, 0.0, 1.0);
    glUseProgram(program);

    glEnable(GL_DEPTH_TEST);
    glm::mat4 viewMatrix = glm::mat4(1.0f);
    glm::mat4 projectionMatrix = glm::mat4(1.0f);
    glm::vec3 v = glm::vec3(cameraFront.x - cameraPos.x, cameraFront.y - cameraPos.y,
                            cameraFront.z - cameraPos.z);
    viewMatrix = glm::lookAt(cameraPos, v, glm::vec3(0.0f, 1.0f, 0.0f));
    projectionMatrix = glm::perspective(glm::radians(45.0f), width / height, 0.1f,
                                        100.0f);
    int viewMatrixLocation = glGetUniformLocation(program, "view");
    int projectMatrixLocation = glGetUniformLocation(program, "projection");
    glUniformMatrix4fv(viewMatrixLocation, 1, GL_FALSE, &viewMatrix[0][0]);
    glUniformMatrix4fv(projectMatrixLocation, 1, GL_FALSE, &projectionMatrix[0][0]);

    glBindVertexArray(VAO);
    glBindTexture(GL_TEXTURE_CUBE_MAP, texture);
    glDrawArrays(GL_TRIANGLES, 0, 36);
}

JNIEXPORT void JNICALL
Java_com_skateboard_skybox_SkyBoxRender_rotate(JNIEnv *env, jobject thiz,jfloat pitch,jfloat yaw) {

    if(pitch>89)
    {
        pitch=89.0;
    }
    if(pitch<-89)
    {
        pitch=-89.0;
    }
    cameraFront.x=glm::cos(glm::radians(pitch))*glm::cos(glm::radians(yaw));
    cameraFront.y=glm::sin(glm::radians(pitch));
    cameraFront.z=glm::cos(glm::radians(pitch))*glm::sin(glm::radians(yaw));
    cameraFront=glm::normalize(cameraFront);
}

}

