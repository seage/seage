#!/bin/bash

if [ $# -eq 0 ]; then
  echo "Usage: makerelease {release-version}"
  exit 0	
fi

VERSION="$1"
OUTPUT="seage-$VERSION"
echo $OUTPUT


if [ -d $OUTPUT ]; 
then
  rm -rf $OUTPUT
  rm -rf $OUTPUT.zip  
fi

mkdir $OUTPUT
mkdir $OUTPUT/bin
mkdir $OUTPUT/problems

cp include/readme.txt $OUTPUT
cp include/cpl-v10.txt $OUTPUT

svn up ..

PROBLEM="seage.problems"
PDIR="../$PROBLEM"
PROJECTS=(seage.aal seage.metaheuristics seage.misc)

for name in ${PROJECTS[*]}; do 
  ant -f "../$name/build.xml" > release.log
  cp -r ../$name/dist/*.jar "$OUTPUT/bin"
  #cp -r ../$name/dist/lib/ $VERSION/seage  
done

for i in $( ls $PDIR); do
  if [ -e $PDIR/$i/build.xml ]; then
    echo $i
    ant -f $PDIR/$i/build.xml >> release.log
    mkdir $OUTPUT/problems/$i
    mkdir $OUTPUT/problems/$i/bin
    cp -r $PDIR/$i/dist/*.jar $OUTPUT/problems/$i/bin
    cp -r $PDIR/$i/dist/lib $OUTPUT/problems/$i/bin
    cp -r $PDIR/$i/data $OUTPUT/problems/$i
    rm -rf $OUTPUT/problems/$i/data/.svn
    cp -r $PDIR/$i/config.xml $OUTPUT/problems/$i

    RUN="$OUTPUT/problems/$i/run.sh"
    touch $RUN
    chmod +x $RUN

    echo "#!/bin/bash" > $RUN
    echo -n "java -jar " >> $RUN    
    cd $OUTPUT/problems/$i/
    echo -n "`find bin -maxdepth 1 -name \*.jar` config.xml" >> run.sh
    cd ../../..
  fi
  
done

zip -r $OUTPUT.zip $OUTPUT >> release.log
rm -rf $OUTPUT