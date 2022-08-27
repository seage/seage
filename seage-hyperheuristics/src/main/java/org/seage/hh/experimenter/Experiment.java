package org.seage.hh.experimenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public interface Experiment {
  static Logger logger =
      LoggerFactory.getLogger(Experimenter.class.getName());

  Double run() throws Exception;
}
