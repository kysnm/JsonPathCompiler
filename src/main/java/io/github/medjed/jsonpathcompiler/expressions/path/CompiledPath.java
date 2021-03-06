/*
 * Copyright 2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.medjed.jsonpathcompiler.expressions.path;

import io.github.medjed.jsonpathcompiler.expressions.Path;
import io.github.medjed.jsonpathcompiler.Configuration;
import io.github.medjed.jsonpathcompiler.expressions.EvaluationAbortException;
import io.github.medjed.jsonpathcompiler.expressions.EvaluationContext;
import io.github.medjed.jsonpathcompiler.expressions.PathRef;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompiledPath implements Path
{

    private static final Logger logger = LoggerFactory.getLogger(CompiledPath.class);

    private final RootPathToken root;

    private final boolean isRootPath;


    public CompiledPath(RootPathToken root, boolean isRootPath) {
        this.root = root;
        this.isRootPath = isRootPath;
    }

    @Override
    public EvaluationContext evaluate(Object document, Object rootDocument, Configuration configuration, boolean forUpdate) {
        if (logger.isDebugEnabled()) {
            logger.debug("Evaluating path: {}", toString());
        }

        EvaluationContextImpl ctx = new EvaluationContextImpl(this, rootDocument, configuration, forUpdate);
        try {
            PathRef op = ctx.forUpdate() ?  PathRef.createRoot(rootDocument) : PathRef.NO_OP;
            root.evaluate("", op, document, ctx);
        } catch (EvaluationAbortException abort){};

        return ctx;
    }

    @Override
    public EvaluationContext evaluate(Object document, Object rootDocument, Configuration configuration){
        return evaluate(document, rootDocument, configuration, false);
    }

    @Override
    public boolean isDefinite() {
        return root.isPathDefinite();
    }

    @Override
    public boolean isFunctionPath() {
        return root.isFunctionPath();
    }

    @Override
    public String toString() {
        return root.toString();
    }

    @Override
    public boolean isRootPath() {
        return isRootPath;
    }

    @Override
    public RootPathToken getRoot() { return root; }

    @Override
    public String getParentPath() {
        return StringUtils.removeEnd(root.toString(), root.getTailPath());
    }

    @Override
    public PathToken getTail() {
        return root.getTail();
    }

    @Override
    public String getTailPath() {
        return root.getTailPath();
    }
}
