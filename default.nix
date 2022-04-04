with import <nixpkgs> { };
let
  millJDK17 = pkgs.mill.override { jre = pkgs.jdk17; };
  scalaJDK17 = pkgs.scala.override { jre = pkgs.jdk17; };
  sbtJDK17 = pkgs.sbt.override { jre = pkgs.jdk17; };
in
mkShell rec {

  name = "cerebral-tutorial";

  buildInputs = [
    bash
    gnumake
    yarn
    nodejs-14_x
    ammonite
    yarn
    coursier
    google-chrome
    chromedriver
    millJDK17
    scalaJDK17
    sbtJDK17
    # nodePackages.webpack
    # nodePackages.webpack-cli
  ];

  shellHook = ''
    echo "environtment loaded and ready .."

    # if [[ ! -d ./node_modules ]]; then
    #   yarn install --frozen-lockfile --non-interactive && yarn cache clean
    # fi

    # export DEVELOP="true"
    # echo "starting yarn"
    # # yarn dev    
    # webpack --mode development --watch --hide-modules        
    ! [[ -z $SNOWPACK ]] && npx snowpack dev || echo "IDE env"
  '';

}
