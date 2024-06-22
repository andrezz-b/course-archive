import { Card, CardContent, CardDescription } from "@/components/ui/card.tsx";
import { useParams, useSearchParams } from "react-router-dom";
import { MaterialGroupService } from "@/api/material-group.service.ts";
import { keepPreviousData } from "@tanstack/react-query";
import { MaterialGroup } from "@/types/MaterialGroup.ts";
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion.tsx";
import useLocalStorage from "@/hooks/useLocalStorage.ts";
import { useMemo, useState } from "react";
import Loading from "@/components/Loading.tsx";
import MaterialItem from "@/components/material/MaterialItem.tsx";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog.tsx";
import { Button } from "@/components/ui/button.tsx";
import MaterialCreateForm from "@/components/material/MaterialCreateForm.tsx";
import { Input } from "@/components/ui/input.tsx";
import { TagService } from "@/api/tag.service.ts";
import { MultiSelect } from "@/components/ui/multi-select.tsx";
import { Separator } from "@/components/ui/separator.tsx";
import { debounce } from "lodash-es";
import { CourseYearService } from "@/api/course-year.service.ts";

const CourseYearPage = () => {
  const [materialFormOpen, setMaterialFormOpen] = useState(false);
  const { courseYearId } = useParams<{ courseYearId: string }>();
  const defaultValue = useMemo(() => ({}), []);
  const [openValues, setOpenValues] = useLocalStorage<Record<string, Array<string>>>(
    "open-group-values",
    defaultValue,
  );

  const tagsQuery = TagService.useGetAll({
    courseYearId,
  });
  const tagOptions = useMemo(() => {
    return (
      tagsQuery.data?.map((tag) => ({
        label: tag.name,
        value: tag.id.toString(),
      })) ?? []
    );
  }, [tagsQuery.data]);

  const currentOpenValues = useMemo(() => {
    if (!openValues) {
      return [];
    }
    if (!openValues[courseYearId!]) {
      return [];
    }
    return openValues[courseYearId!];
  }, [courseYearId, openValues]);
  const updateValues = (values: Array<string>) => {
    setOpenValues({
      ...openValues,
      [courseYearId!]: values,
    });
  };

  const [searchParams, setSearchParams] = useSearchParams({
    tagIds: [],
    materialName: "",
  });

  const updateMaterialName: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    setMaterialName(e.target.value);
    updateMaterialNameParam(e.target.value);
  };

  const updateMaterialNameParam = useMemo(
    () =>
      debounce(
        (value) =>
          setSearchParams((prev) => {
            prev.set("materialName", value);
            if (!value) prev.delete("materialName");
            return prev;
          }),
        500,
      ),
    [setSearchParams],
  );
  const updateSelectedTags = (tags: Array<string>) => {
    setSearchParams((prev) => {
      prev.delete("tagIds");
      tags.forEach((tag) => prev.append("tagIds", tag));
      return prev;
    });
    setSelectedTags(tags);
  };

  const [selectedTags, setSelectedTags] = useState<Array<string>>(searchParams.getAll("tagIds")!);
  const [materialName, setMaterialName] = useState<string>(searchParams.get("materialName")!);
  const groupQuery = MaterialGroupService.useGetAll(
    {
      courseYearId,
      materialName: searchParams.get("materialName"),
      size: 100,
      tagIds: searchParams.getAll("tagIds"),
    },
    {
      placeholderData: keepPreviousData,
    },
  );

  return (
    <Card className="w-full md:px-10 border-none space-y-4 relative">
      <GeneralInfo courseYearId={courseYearId} />
      <div className="flex justify-between flex-col md:flex-row md:items-center">
        <div className="flex flex-col md:flex-row md:h-10 gap-1">
          <Input
            placeholder="Search..."
            className="md:w-[250px]"
            value={materialName}
            onChange={updateMaterialName}
          />
          <MultiSelect
            className="py-1"
            options={tagOptions}
            onValueChange={updateSelectedTags}
            defaultValue={selectedTags}
            placeholder="Tags"
          />
        </div>
        <Separator className="md:hidden my-5" />
        <Dialog open={materialFormOpen} onOpenChange={setMaterialFormOpen}>
          <DialogTrigger asChild>
            <Button
              className="flex justify-center my-2 max-w-fit"
              onClick={() => setMaterialFormOpen(true)}
            >
              Add new materials
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Add new material</DialogTitle>
            </DialogHeader>
            <MaterialCreateForm closeDialog={() => setMaterialFormOpen(false)} />
          </DialogContent>
        </Dialog>
      </div>
      <CardContent className="flex flex-col gap-8 p-0">
        <Accordion
          type="multiple"
          value={currentOpenValues}
          onValueChange={updateValues}
          defaultValue={currentOpenValues}
        >
          {groupQuery.isLoading ? (
            <Loading />
          ) : !groupQuery.data?.content.length ? (
            <span>No materials available yet</span>
          ) : null}
          {groupQuery.data?.content.map((group) => (
            <MaterialGroupCard key={group.id} group={group} />
          ))}
        </Accordion>
      </CardContent>
    </Card>
  );
};

interface MaterialGroupCardProps {
  group: MaterialGroup;
}

const MaterialGroupCard = ({ group }: MaterialGroupCardProps) => {
  return (
    <AccordionItem value={group.id.toString()}>
      <AccordionTrigger>
        <h3 className="text-2xl">
          {group.name}
          <span className="text-muted-foreground"> ({group.materials.length})</span>
        </h3>
      </AccordionTrigger>
      <AccordionContent>
        {group.description && <CardDescription>{group.description}</CardDescription>}
        <div>
          {!group.materials?.length && <div className="pt-2 text-lg">No materials found</div>}
          {group.materials?.map((material) => (
            <MaterialItem key={material.id} material={material} />
          ))}
        </div>
      </AccordionContent>
    </AccordionItem>
  );
};

interface GeneralInfoProps {
  courseYearId?: string;
}

const GeneralInfo = ({ courseYearId }: GeneralInfoProps) => {
  const { data: courseYear } = CourseYearService.useGetById(
    courseYearId ? parseInt(courseYearId) : undefined,
  );
  return (
    <Accordion type="single" collapsible>
      <AccordionItem value="general-info">
        <AccordionTrigger>
          <h3 className="text-lg">General Information</h3>
        </AccordionTrigger>
        <AccordionContent>
          {courseYear && (
            <div className="flex flex-col gap-1">
              <InfoItem title="Professor" value={courseYear.professor} />
              <InfoItem title="Assistant" value={courseYear.assistant} />
              <InfoItem title="Enrolled" value={courseYear.enrollmentCount} />
              <InfoItem title="Passed" value={courseYear.passedCount} />
              <InfoItem title="Lectures" value={courseYear.lectureCount} />
              <InfoItem title="Labs" value={courseYear.laboratoryCount} />
              <InfoItem title="Excresises" value={courseYear.exerciseCount} />
            </div>
          )}
        </AccordionContent>
      </AccordionItem>
    </Accordion>
  );
};

const InfoItem = ({ title, value }: { title: string; value?: number | string | null }) => {
  if (value === undefined || value === null) return null;
  if (typeof value === "string" && value.trim() === "") return null;
  return (
    <div>
      {title}: <strong>{value}</strong>
    </div>
  );
};

export default CourseYearPage;
