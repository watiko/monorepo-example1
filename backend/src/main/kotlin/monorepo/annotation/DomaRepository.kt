package monorepo.annotation

import org.seasar.doma.AnnotateWith
import org.seasar.doma.Annotation
import org.seasar.doma.AnnotationTarget

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

// https://kinjouj.github.io/2015/03/doma-part2-di-container.html
@AnnotateWith(
    annotations = [
        // 生成されたDAO実装クラスに@Component
        Annotation(target = AnnotationTarget.CLASS, type = Component::class),
        // 生成されたDAO実装クラスのコンストラクタに@Autowired
        Annotation(target = AnnotationTarget.CONSTRUCTOR, type = Autowired::class)]
)
annotation class DomaRepository
