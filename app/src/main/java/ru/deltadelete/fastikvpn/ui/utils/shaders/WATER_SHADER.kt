package ru.deltadelete.fastikvpn.ui.utils.shaders

import org.intellij.lang.annotations.Language

/**
 * ИИ слоп, анимация зацикливается криво.
 * TODO: Переписать самому
 */
@Language("AGSL")
val WATER_SHADER = """
uniform float2 uSize;       // Размеры Canvas
uniform float uTime;       // Время для анимации
uniform float2 uGravity;    // Вектор акселерометра (X, Y)

half4 main(float2 fragCoord) {
    // 1. Нормализация координат (от -1.0 до 1.0)
    float2 uv = (fragCoord - uSize * 0.5) / min(uSize.x, uSize.y) * 2.0;

    // 2. Параметры круглого сосуда
    float radius = 0.9;
    float distToCenter = length(uv);
    
    if (distToCenter > radius) {
        float edge = smoothstep(radius, radius + 0.03, distToCenter);
        return mix(half4(0.2, 0.2, 0.2, 0.1), half4(0.0), edge);
    }

    // 3. Вычисление направления «верха» без использования atan2
    // Нормализуем вектор гравитации, чтобы получить направление «низа»
    float2 gravityDir = normalize(uGravity + float2(0.0001, 0.0001)); // Защита от деления на ноль
    
    // В Android акселерометр инвертирован, поэтому инвертируем оси для правильной физики
    float2 down = float2(-gravityDir.x, gravityDir.y);
    // Вектор «право» перпендикулярен вектору «направление вниз»
    float2 right = float2(down.y, -down.x);

    // Проецируем координаты uv на наши новые оси наклона (вместо поворота через матрицы/углы)
    float2 rotatedUV = float2(dot(uv, right), dot(uv, down));

    // 4. Генерируем волны
    float wave = sin(rotatedUV.x * 4.0 + uTime * 2.0) * 0.04 
               + cos(rotatedUV.x * 8.0 - uTime * 1.5) * 0.02;

    float waterLevel = 0.0 + wave;

    // 5. Логика пузырька воздуха
    float bubbleX = sin(uTime * 1.0) * 0.2; 
    float2 bubbleCenter = float2(bubbleX, waterLevel - 0.15); 
    float bubbleRadius = 0.12;
    float distToBubble = length(rotatedUV - bubbleCenter);

    // 6. Цвета слоев
    half4 waterColor = half4(0.0, 0.5, 0.8, 0.8); 
    half4 airColor = half4(0.9, 0.9, 0.9, 0.2);   
    half4 bubbleColor = half4(0.8, 0.9, 1.0, 0.6); 

    half4 finalColor = waterColor;

    // Проверяем уровень воды (в новой системе координат rotatedUV.y идет сверху вниз)
    if (rotatedUV.y < waterLevel) {
        // Выше уровня воды -> Воздух
        finalColor = airColor;
    } else {
        // Ниже уровня воды -> Вода + Пузырек
        if (distToBubble < bubbleRadius) {
            float bubbleEdge = smoothstep(bubbleRadius - 0.03, bubbleRadius, distToBubble);
            finalColor = mix(airColor, bubbleColor, bubbleEdge);
        }
    }

    // Внутреннее свечение стенок сосуда
    float glassGlow = smoothstep(radius - 0.05, radius, distToCenter);
    finalColor = mix(finalColor, half4(1.0, 1.0, 1.0, 0.4), glassGlow * 0.5);

    return finalColor;
}
""".trimIndent()