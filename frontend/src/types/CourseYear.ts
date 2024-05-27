import { z } from "zod";
import { Course } from "./Course";

export interface CourseYear {
  id: number;
  course?: Course;
  academicYear: string;
  professor: string | null;
  assistant: string | null;
  difficulty: number | null;
  enrollmentCount: number | null;
  passedCount: number | null;
  lectureCount: number | null;
  exerciseCount: number | null;
  laboratoryCount: number | null;
}

export const CourseYearEditSchema = z.object({
  professor: z.string().max(256, "Professor names must be at most 256 characters").optional(),
  assistant: z.string().max(256, "Assistant names must be at most 256 characters").optional(),
  difficulty: z.coerce
    .number({
      invalid_type_error: "Difficulty must be a number",
    })
    .min(1, "Difficulty must be at least 1")
    .max(10, "Difficulty must be at most 10")
    .or(z.literal(""))
    .optional(),
  enrollmentCount: z.coerce
    .number({
      invalid_type_error: "Enrollment count must be a number",
    })
    .positive()
    .or(z.literal(""))
    .optional(),
  passedCount: z.coerce
    .number({
      invalid_type_error: "Passed count must be a number",
    })
    .positive()
    .or(z.literal(""))
    .optional(),
  lectureCount: z.coerce
    .number({
      invalid_type_error: "Lecture count must be a number",
    })
    .positive()
    .or(z.literal(""))
    .optional(),
  exerciseCount: z.coerce
    .number({
      invalid_type_error: "Exercise count must be a number",
    })
    .positive()
    .or(z.literal(""))
    .optional(),
  laboratoryCount: z.coerce
    .number({
      invalid_type_error: "Laboratory count must be a number",
    })
    .positive()
    .or(z.literal(""))
    .optional(),
});

export const CourseYearCreateSchema = z
  .object({
    academicYear: z.string().regex(/^\d{4}\/\d{4}$/, "Must be in the format YYYY/YYYY"),
    courseId: z.coerce
      .number({
        invalid_type_error: "Course ID must be a number",
        required_error: "Course ID is required",
      })
      .positive("Course ID must be a positive number"),
  })
  .merge(CourseYearEditSchema);

export type CourseYearCreateData = z.infer<typeof CourseYearCreateSchema>;
export type CourseYearEditData = z.infer<typeof CourseYearEditSchema>;
