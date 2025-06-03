package com.aiyuns.DJL;

import ai.djl.engine.Engine;

public class DJLSetupTest {

  public static void main(String[] args) {
    System.out.println("Default Engine: " + Engine.getDefaultEngineName());
    System.out.println("Available Engines: " + Engine.getAllEngines());
  }
}
