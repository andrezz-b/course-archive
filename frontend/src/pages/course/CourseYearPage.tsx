import { Card, CardContent, CardDescription } from "@/components/ui/card.tsx";
import { useParams } from "react-router-dom";
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

const CourseYearPage = () => {
  const [materialFormOpen, setMaterialFormOpen] = useState(false);
  const { courseYearId } = useParams<{ courseYearId: string }>();
  const [selectedTags, setSelectedTags] = useState<Array<string>>([]);
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

  const groupQuery = MaterialGroupService.useGetAll(
    {
      courseYearId,
      size: 100,
    },
    {
      placeholderData: keepPreviousData,
    },
  );

  return (
    <Card className="w-full md:px-10 border-none space-y-4 relative">
      <div className="flex justify-between flex-col md:flex-row md:items-center">
        <div className="flex flex-col md:flex-row md:h-10 gap-1">
          <Input placeholder="Search..." className="md:w-[250px]" />
          <MultiSelect
            className="py-1"
            options={tagOptions}
            onValueChange={setSelectedTags}
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
        <Accordion type="multiple" value={currentOpenValues} onValueChange={updateValues}>
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
        <h3 className="text-2xl">{group.name}</h3>
      </AccordionTrigger>
      <AccordionContent>
        {group.description && <CardDescription>{group.description}</CardDescription>}
        <div>
          {group.materials?.map((material) => (
            <MaterialItem key={material.id} material={material} />
          ))}
        </div>
      </AccordionContent>
    </AccordionItem>
  );
};

export default CourseYearPage;
